# meme-rand
A small app that provides the user some random Memes from reddit and lets them share it.

Download APK:
<a href="https://drive.google.com/drive/folders/1bd00WpQWj2MC41BG9evqF-LK94OzMN7j?usp=sharing">Click Here</a><br>
<br>
--> UI Elements include: the ConstraintLayout, two Buttons, an ImageView, a Snackbar, the app icon, a progress bar.<br>
<br>
<br>
<b>Added in v1.2:</b><br>
A Three Dot Menu, Download Button, Info Activity, Info Menu Button, and a Previous Button<br>
<b>Added in v1.2.1</b><br>
File Name Improvements, Toast on Download and Avoid the memes which the user has already seen in that Instance
<br>
<br>

The app basically uses Volley API to request response from http and Sends a JSONObjectRequest to the <a href="https://github.com/D3vd/Meme_Api">Meme API</a>
to get data and reads the "url" attribute from to send it to ImageView using <a href="https://github.com/bumptech/glide">Glide</a> and kotlinx library.

