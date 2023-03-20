# ReflectionWebServer
Uses reflection to find Strings within obfuscated files 
## Security consideration
This program executes malware code within your computer. You should run it on a VM or ideally a Docker container

## How to run
You need both Python 3 and ideally Java 8.<br>
Execute
```sh
python -m pip install -r requirements.txt
javac *.java
python main.py
```
Set the environment variable DISCORD_WEBHOOK_URL to get alerts when a webhook gets deleted
