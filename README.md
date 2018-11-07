# Trello changelog

Pretty much every product we deal with has a requirement to inform end users when we change things.
We use Trello internally but don't necessarily what to make our Trello boards public.

This service compiles a public facing changelog by extracting Trello cards which are in the 'Done' list and have been
explictly labeled as public.


## Usage

You will need to supply Trello API credentials, the name of your 'Done' list and the name of the label you use to indicate that a card
is public and should be included in the changelog.

In return you will get a list of changelog items sorted in date descending order. The public date is the date the card was moved into the done column.


## Output

A list of [ChangeLogItem](app/model/ChangeLogItem.scala) in date descending order.

```
[
  {
    "title": "Bug fix. Widget ordering",
    "description": "The widget thingamagig now outputs content right side round.",
    "date": "2018-10-16T18:29:50.828Z"
  },
  {
    "title": "Widgets now available",
    "description": "The system can now produce widgets.",
    "date": "2018-09-09T19:55:37.025Z"
  }
]
```


## Build

Scala / Playframework / sbt with Docker plugin enabled.

```
sbt clean docker:publishLocal
```

