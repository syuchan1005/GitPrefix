#!/usr/bin/env python

import configparser
import os
import urllib.request

import bs4
import requests


def removeExt(file):
    return file.replace(".png", "")


def gmojiAlias(gmoji):
    return gmoji.get('alias')


def gmojiText(gmoji):
    return gmoji.text


if __name__ == "__main__":
    config = configparser.ConfigParser()
    config.read('./EmojiUpdate.ini', 'UTF-8')
    os.system('git submodule update --recursive')

    # make folders
    fileList = list(map(removeExt, os.listdir("./emoji-cheat-sheet/public/graphics/emojis")))
    fileList.sort()
    os.makedirs(config.get("emoji", "saveFolder"), 0o777, True)

    if config.get('general', 'convertIcon') == 'True':
        for emoji in fileList:
            os.system(f'ffmpeg -y -i {config.get("emoji", "folder")}{emoji}.png -loglevel fatal '
                      f'-vf scale=16:16 {config.get("emoji", "saveFolder")}{emoji}.png')
            os.system(f'ffmpeg -y -i {config.get("emoji", "folder")}{emoji}.png -loglevel fatal '
                      f'-vf scale=32:32 {config.get("emoji", "saveFolder")}{emoji}@2x.png')

    if config.get('general', 'createClass') == 'True':
        # gist
        gistContent = ''
        for emoji in fileList:
            gistContent += f':{emoji}:'
        ses = requests.Session()
        ses.auth = (config.get("github", "username"), config.get("github", "password"))
        res = ses.post('https://api.github.com/gists',
                       json={"files": {"test.md": {"content": gistContent}}})
        resData = res.json()
        html = urllib.request.urlopen(resData["html_url"]).read()
        res = ses.delete(f'https://api.github.com/gists/{resData["id"]}')

        # gist parsing & create emoji list
        soup = bs4.BeautifulSoup(html, features="html.parser")
        gmojis = soup.select(".markdown-body.entry-content > p")[0]
        emojis = {}
        for index, dom in enumerate(gmojis.children):
            emojis[fileList[index]] = f'"{dom.string}"' if type(dom) == bs4.element.Tag else None

        emojiStr = ""
        for k, v in emojis.items():
            emojiStr += f'        {config.get("class", "mapName")}' \
                        f'.put("{k}", new EmojiData({v or "null"}, IconLoader.getIcon("/emojis/{k}.png")));\n'
        with open(f'./src/main/java/{config.get("class", "package").replace(".", "/")}/{config.get("class", "name")}.java',
                  mode='w') as f:
            f.write(f'''package {config.get("class", "package")};

import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;

import com.intellij.openapi.util.IconLoader;

public class {config.get("class", "name")} {{
    private static Map<String, EmojiData> {config.get("class", "mapName")} = new HashMap<>({len(emojis) + 1}, 1F); // true length = {len(emojis)} 
    
    static {{
{emojiStr[:-1]}
    }}

    public static class EmojiData {{
        private String code;
        private Icon icon;
        
        private EmojiData(String code, Icon icon) {{
            this.code = code;
            this.icon = icon;
        }}
        
        public String getCode() {{
            return this.code;
        }}
        
        public Icon getIcon() {{
            return this.icon;
        }}
    }}
    
    public static Map<String, EmojiData> getEmojiMap() {{
        return {config.get("class", "mapName")};
    }}

    public static Icon getIcon(String emoji) {{
        return {config.get("class", "mapName")}.get(emoji).getIcon();
    }}
}}
    ''')
        f.close()
