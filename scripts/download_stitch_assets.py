import os
import json
import urllib.request
import urllib.error

PROJECT_ID = "479458156171166725"
API_KEY = os.getenv("STITCH_API_KEY")
BASE_URL = f"https://stitch.googleapis.com/v1/projects/{PROJECT_ID}/screens"

SCREENS = {
    "login_screen": "ccbfc7fc57ff450b918efba265a6f8ab",
    "otp_verification": "36175135a9ad44a393794d25d61f6566",
    "signup_screen": "bd108e471cba431eb95deb5b6e78c46d",
    "forgot_password": "a9efbe3a300746bfaf3af855f0f089c9",
    "student_dashboard": "a40b9ce075314ba9a9ec057f46d00ccf",
    "project_details": "a528a22f0f584b4897d8d31a86ee1b12",
    "find_an_advisor": "b84d7a102ceb4d768a862485c3219db1",
    "my_requests": "8b12c07c2747490fa26ab109994b8c4e",
    "advisor_dashboard": "22da06ceb877493992a7f0a2812b6499",
    "requests_management": "cc32a02a77024c1d9164b1b6ad3d5908",
    "admin_overview": "f8b802a32d784c6893857147264670ee",
    "user_management": "056f50d9f6604d6ab245b7bc66a8491c",
    "faculty_management": "62fde8de8aa34c1cb27cb99d1b4b3be2"
}

OUTPUT_DIR = os.path.join(os.getcwd(), "stitch_designs")
os.makedirs(OUTPUT_DIR, exist_ok=True)

def download_file(url, filepath):
    print(f"Downloading {url} to {filepath}")
    try:
        urllib.request.urlretrieve(url, filepath)
    except Exception as e:
        print(f"Failed to download {url}: {e}")

def get_screen_details(screen_id):
    if not API_KEY:
        print("Missing STITCH_API_KEY environment variable")
        return None

    url = f"{BASE_URL}/{screen_id}"
    req = urllib.request.Request(url)
    req.add_header("X-Goog-Api-Key", API_KEY)
    try:
        with urllib.request.urlopen(req) as response:
            return json.loads(response.read().decode())
    except urllib.error.URLError as e:
        print(f"Failed to get details for {screen_id}: {e}")
        return None

def main():
    for name, screen_id in SCREENS.items():
        print(f"Processing {name} ({screen_id})...")
        details = get_screen_details(screen_id)
        if not details:
            continue
        
        # Download HTML
        html_url = details.get("htmlCode", {}).get("downloadUrl")
        if html_url:
            download_file(html_url, os.path.join(OUTPUT_DIR, f"{name}.html"))
        
        # Download Screenshot
        img_url = details.get("screenshot", {}).get("downloadUrl")
        if img_url:
            # Append width as suggested by skill
            width = details.get("width", "1280")
            img_url_with_width = f"{img_url}=w{width}"
            download_file(img_url_with_width, os.path.join(OUTPUT_DIR, f"{name}.png"))

if __name__ == "__main__":
    main()
