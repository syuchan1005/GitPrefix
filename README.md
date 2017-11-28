# EmojiPrefix
This can make 'Emoji Prefix Commit' easy.

[IntelliJ Plugin Page](https://plugins.jetbrains.com/plugin/9725-emojiprefix)

## Getting Started (in 3 step)
1. '.emojirc' create your project root.
2. Write emojis(emoticons) you want to use
3.After that, just commit

### .emojirc sample
```
:bug:     BugFix 
:lock:    SecurityFix # Comment
:recycle: Refactoring
:books:   Documentation
# Comment
```

## Features
- Input Completion(All Files)
- VCS CommitPanel show emoji
- VCS CommitMessage show preview icon

## emojiList
[emoji-cheat-sheet](https://github.com/WebpageFX/emoji-cheat-sheet.com/tree/master/public/graphics/emojis) scraping it.

```js:use-script.js
var aliases = [];

document.querySelectorAll("td.content > span > .js-navigation-open")
.forEach((v) => aliases.push(v.title.slice(0,-4)));

var str = "";
for (var alias of aliases) {
    str += '\"' + alias + '\",'
}
console.log(str.slice(0, -1));
```

## License
MIT
