#!/usr/bin/env python

import os

if __name__ == "__main__":
    os.system('git submodule update --recursive')
    list = os.listdir("./emoji-cheat-sheet/public/graphics/emojis")
    l = str(len(list))
    c = 1
    for file in list:
        if file[-3:] == "png":
            os.system('ffmpeg -y -i ./emoji-cheat-sheet/public/graphics/emojis/' + file + " -loglevel fatal -vf scale=16:16 ./resources/emojis/" + file)
            # os.system('ffmpeg -y -i ./emoji-cheat-sheet/public/graphics/emojis/' + file + " -loglevel fatal -vf scale=16:16 ./resources/emojis/" + file.replace(".png", "_dark.png"))
            os.system('ffmpeg -y -i ./emoji-cheat-sheet/public/graphics/emojis/' + file + " -loglevel fatal -vf scale=32:32 ./resources/emojis/" + file.replace(".png", "@2x.png"))
            # os.system('ffmpeg -y -i ./emoji-cheat-sheet/public/graphics/emojis/' + file + " -loglevel fatal -vf scale=32:32 ./resources/emojis/" + file.replace(".png", "@2x_dark.png"))
            print(str(c) + "/" + l)
            c += 1
