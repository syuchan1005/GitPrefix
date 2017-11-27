# EmojiPrefix
This is a Plugin to easily create an Emoji Prefix Commit.

[IntelliJ Plugin Page](https://plugins.jetbrains.com/plugin/9725-emojiprefix)

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