#!/usr/bin/env python

import os

emojiFolder = "./emoji-cheat-sheet/public/graphics/emojis/"
saveEmojiFolder = "./src/main/resources/emojis/"

if __name__ == "__main__":
    os.system('git submodule update --recursive')
    list = os.listdir("./emoji-cheat-sheet/public/graphics/emojis")
    l = str(len(list))
    aStr = "String[] emojiNames = new String[] {"
    c = 1

    os.mkdir(saveEmojiFolder)
    for file in list:
        if file[-3:] == "png":
            os.system(f'ffmpeg -y -i {emojiFolder}{file} -loglevel fatal -vf scale=16:16 {saveEmojiFolder}{file}')
            os.system(f'ffmpeg -y -i {emojiFolder}{file} -loglevel fatal -vf scale=32:32 {saveEmojiFolder}{file.replace(".png", "@2x.png")}')
            aStr += '"' + file.replace(".png", "") + '", '
            print(str(c) + "/" + l)
            c += 1
    aStr += "}"
    print(aStr)
