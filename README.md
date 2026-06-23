# Polymorphic JSON with Jackson: Deserializing GitHub Events

GitHub's events API returns a mixed array. Every entry carries a `type` field ("PushEvent", "PullRequestEvent", "DeleteEvent", and so on) that tells you what the rest of the object means. The goal is to read that array into the right Java type per entry without hand-writing a switch on the `type` string. Jackson's polymorphic type handling does exactly this.

## The setup

A sealed interface defines the closed set of event types, and one annotation tells Jackson how to pick a concrete type for each entry.

```java
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public sealed interface GitHubEvent permits DeleteEvent, PullRequestEvent {
    static List<GitHubEvent> fromJson(String json) {
        return JsonMapper.builder().build()
            .readerForListOf(GitHubEvent.class)
            .readValue(json);
    }
}
```

Read the annotation as three independent settings:

- `property = "type"` is the field Jackson looks at. With `As.PROPERTY` it is a sibling field sitting next to the event's own data.
- `use = Id.NAME` says the value of that field is a logical name, not a class reference.

## How a single entry resolves

On read, Jackson handles each object in the array the same way. It finds the `type` field, takes its string value (say "DeleteEvent"), looks up which permitted subtype answers to that name, and deserializes the object into that class. The direction is worth being precise about: it reads the value out of the JSON first, then finds the matching type. It is not comparing your class names against the document.

## Why Id.NAME and not a class-based id

`Id.NAME` keeps the wire format independent of your Java layout. The `type` string is a contract you share with GitHub, while your package names and class structure stay private to your code.

The class-based options behave very differently. `Id.CLASS` expects the fully qualified class name in the JSON, and `Id.MINIMAL_CLASS` expects a package-relative name with a leading dot, like `.DeleteEvent`, which it expands back to `couryrr.github.DeleteEvent`. Point either of them at GitHub's plain "DeleteEvent" and resolution fails:

```
InvalidTypeIdException: Could not resolve type id 'DeleteEvent' as a subtype of
couryrr.github.GitHubEvent: no such class found
```

A bare "DeleteEvent" with no leading dot is read as a fully qualified class name, so Jackson tries to load a class literally called `DeleteEvent` in no package and finds nothing. Class-based ids really only fit Java to Java serialization inside one codebase, and they carry a security validator because they load arbitrary classes by name. For consuming an external API, `Id.NAME` is the right tool.

## Where the names come from

Two separate things happen here, and it helps to keep them apart.

Discovery is the set of candidate classes. Because `GitHubEvent` is sealed, Jackson 3.0 reads the `permits` clause and registers `DeleteEvent` and `PullRequestEvent` on its own. In older versions you listed them by hand with `@JsonSubTypes`.

The `permits` clause does not assign names. When a subtype has no `@JsonTypeName`, Jackson defaults the name to the simple class name. That is the whole reason this works with no extra annotations: the record `DeleteEvent` defaults to the name "DeleteEvent", which is exactly what GitHub puts in the `type` field.

The catch is that the default ties the wire value to the class name. Rename the `DeleteEvent` record and the expected `type` value changes with it. If you want the contract fixed regardless of refactoring, pin it:

```java
@JsonTypeName("DeleteEvent")
public record DeleteEvent(...) implements GitHubEvent { }
```

## @JsonSubTypes use cases

With a sealed type you no longer need `@JsonSubTypes` for discovery, but it is not gone. It is additive and per entry rather than a master switch, so reach for it (or for `@JsonTypeName`) when:

- A wire value differs from the simple class name and you want to set it explicitly.
- A single type needs to accept several discriminator values, which the `names` attribute supports.
- The base type is not sealed, so there is no `permits` clause and discovery has to come from somewhere.

Listing a class without a `name` still falls back to the simple-name default, so adding the annotation does not force you to name every type.

## Handling event types you don't model

GitHub adds new event types over time, and your sealed set only covers the ones you care about. By default an unrecognized `type` value throws `InvalidTypeIdException`. To absorb the rest instead, give the annotation a fallback with `defaultImpl`, pointing it at a catch-all event:

```java
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY,
              property = "type", visible = true, defaultImpl = UnknownEvent.class)
public sealed interface GitHubEvent permits DeleteEvent, PullRequestEvent, UnknownEvent {
    // ...
}

public record UnknownEvent(String type) implements GitHubEvent { }
```

Because `GitHubEvent` is sealed, the fallback has to be a permitted subtype. `UnknownEvent` must appear in the `permits` clause and implement the interface; a catch-all sitting outside the sealed set will not compile as an implementer. `defaultImpl` also covers the case where the `type` field is missing entirely, not just unrecognized.

The `visible = true` part is easy to miss and worth calling out. With `As.PROPERTY`, Jackson consumes the `type` field while resolving the type and then removes it from the stream, so an `UnknownEvent(String type)` component has nothing to bind to and comes out null. Setting `visible = true` leaves the discriminator in the stream after resolution so it can also populate a matching field or record component. The known events now receive a `type` property too, which is harmless in Jackson 3.0 since failing on unknown properties is off by default.

If you would rather drop unknown entries than capture them, disabling `DeserializationFeature.FAIL_ON_INVALID_SUBTYPE` makes an unresolved id deserialize to null instead of throwing. Note the difference in a `List<GitHubEvent>`: the feature route leaves null elements you filter afterward, while `defaultImpl` leaves `UnknownEvent` instances you can branch on. Neither one skips the element outright.

## Jackson 3.0 notes

The sealed auto-detection arrived in Jackson 3.0, which also made mappers immutable. Construct with `JsonMapper.builder().build()` rather than `new JsonMapper()`, and note that the package moved from `com.fasterxml.jackson` to `tools.jackson`. The annotations stayed on `com.fasterxml.jackson.annotation`, so the `@JsonTypeInfo` import is unchanged. Jackson exceptions also became unchecked in 3.0, which is why `fromJson` no longer needs a `throws` clause.