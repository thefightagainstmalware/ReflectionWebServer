from flask import Flask, request, make_response, redirect
from werkzeug.security import safe_join
from pathlib import Path
import subprocess, html

app = Flask("reflectionwebserver")


@app.route("/")
def index():
    return redirect("/upload")


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

        if file == None:
            return "Unsafe file name detected"

        Path(file).parents[0].mkdir(parents=True, exist_ok=True)

        process = subprocess.Popen(["java", "Main", file], stdout=subprocess.PIPE, stderr=subprocess.PIPE)

        out = process.communicate()
        return f"stdout: {html.escape(str(out[0]))}, stderr: {html.escape(str(out[1]))}"
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
