from flask import Flask, request, make_response
from werkzeug.security import safe_join
from pathlib import Path
import subprocess, os

list_of_subprocesses = []

app = Flask("reflectionwebserver")


def relFilePaths(directory):
    """Get a list of all files with absolute file paths"""
    for dirpath, _, filenames in os.walk(directory):
        for f in filenames:
            yield os.path.relpath(os.path.join(dirpath, f), directory)

def cleanup_subprocesses():
    for i in range(len(list_of_subprocesses)):
        process = list_of_subprocesses[i]
        if process.returncode is None:
            list_of_subprocesses.pop(i)

@app.route("/")
def hi():
    return "hi"


@app.route("/upload", methods=["GET", "POST"])
def upload():
    if request.method == "POST":
        if len(request.files) != 1:
            resp = make_response()
            resp.status_code = 400
            resp.set_data("Wrong number of files.\n")
            return resp

        fileitem = request.files["file"]
        fname = fileitem.filename.replace(".jar", "")
        folder = safe_join(
            "./tmp",
            fname,
        )

        file = safe_join(folder, "tmp.jar")

        try:
            Path(file).parents[0].mkdir(parents=True)
        except FileExistsError:
            pass

        fileitem.save(file)
        subprocess.call(["unzip", "-o", file, "-d", folder])
        for i in relFilePaths(folder):
            if not i.endswith(".class"):
                continue
            subprocess.Popen(
                [
                    "/usr/bin/java",
                    "Main",
                    fname,
                    i.replace("/", ".").replace("class", "").strip("."),
                ]
            )

        return "We do a little trolling"
    else:
        return """
    <!doctype html>
    <title>Upload new File</title>
    <h1>Upload new File</h1>
    <form method=post enctype=multipart/form-data>
      <input type=file name=file>
      <input type=submit value=Upload>
    </form>"""


if __name__ == "__main__":
    app.run("0.0.0.0")
