import requests
import os

screens = [
    {"id": "a40b9ce075314ba9a9ec057f46d00ccf", "title": "Student Dashboard", "url": "https://contribution.usercontent.google.com/download?c=CgthaWRhX2NvZGVmeBJ6Eh1hcHBfY29tcGFuaW9uX2dlbmVyYXRlZF9maWxlcxpZCiVodG1sXzVmNTc5MzY5Y2ZiZTQyOTU5ZmY0Zjc0ZWQ5Nzc4NDYwEgsSBxDg_8qZjgEYAZIBIgoKcHJvamVjdF9pZBIUQhI0Nzk0NTgxNTYxNzExNjY3MjU&filename=&opi=89354086"},
    {"id": "a528a22f0f584b4897d8d31a86ee1b12", "title": "Project Details", "url": "https://contribution.usercontent.google.com/download?c=CgthaWRhX2NvZGVmeBJ6Eh1hcHBfY29tcGFuaW9uX2dlbmVyYXRlZF9maWxlcxpZCiVodG1sX2I5NmIzZjZiNzdjNTQ5YzM4OWY0ZDA0MjkwNzQyZTk1EgsSBxDg_8qZjgEYAZIBIgoKcHJvamVjdF9pZBIUQhI0Nzk0NTgxNTYxNzExNjY3MjU&filename=&opi=89354086"},
    {"id": "b84d7a102ceb4d768a862485c3219db1", "title": "Find an Advisor", "url": "https://contribution.usercontent.google.com/download?c=CgthaWRhX2NvZGVmeBJ6Eh1hcHBfY29tcGFuaW9uX2dlbmVyYXRlZF9maWxlcxpZCiVodG1sX2E0NTYxYzZjN2ViMjRmZWViZDMwODlhYmI0MmM2OTE3EgsSBxDg_8qZjgEYAZIBIgoKcHJvamVjdF9pZBIUQhI0Nzk0NTgxNTYxNzExNjY3MjU&filename=&opi=89354086"},
    {"id": "8b12c07c2747490fa26ab109994b8c4e", "title": "My Requests", "url": "https://contribution.usercontent.google.com/download?c=CgthaWRhX2NvZGVmeBJ6Eh1hcHBfY29tcGFuaW9uX2dlbmVyYXRlZF9maWxlcxpZCiVodG1sXzQ1NTlmYzQ5MTI0YTRkOGFhM2FiNTczYzBhMTQ5NzVhEgsSBxDg_8qZjgEYAZIBIgoKcHJvamVjdF9pZBIUQhI0Nzk0NTgxNTYxNzExNjY3MjU&filename=&opi=89354086"},
    {"id": "22da06ceb877493992a7f0a2812b6499", "title": "Advisor Dashboard", "url": "https://contribution.usercontent.google.com/download?c=CgthaWRhX2NvZGVmeBJ6Eh1hcHBfY29tcGFuaW9uX2dlbmVyYXRlZF9maWxlcxpZCiVodG1sXzZmMjI2OGI5Y2YzMTQxZmFiZDgxZDc1YjUzYWU2YzAzEgsSBxDg_8qZjgEYAZIBIgoKcHJvamVjdF9pZBIUQhI0Nzk0NTgxNTYxNzExNjY3MjU&filename=&opi=89354086"},
    {"id": "cc32a02a77024c1d9164b1b6ad3d5908", "title": "Requests Management", "url": "https://contribution.usercontent.google.com/download?c=CgthaWRhX2NvZGVmeBJ6Eh1hcHBfY29tcGFuaW9uX2dlbmVyYXRlZF9maWxlcxpZCiVodG1sXzIyMDczNDRhMzFhMDQyNzA5NjY1Y2Q1ZjBlYTNlNDQ0EgsSBxDg_8qZjgEYAZIBIgoKcHJvamVjdF9pZBIUQhI0Nzk0NTgxNTYxNzExNjY3MjU&filename=&opi=89354086"},
    {"id": "f8b802a32d784c6893857147264670ee", "title": "Admin Overview", "url": "https://contribution.usercontent.google.com/download?c=CgthaWRhX2NvZGVmeBJ6Eh1hcHBfY29tcGFuaW9uX2dlbmVyYXRlZF9maWxlcxpZCiVodG1sXzczOTRmNTJjOTA4YzRjY2M5NzMzZTA3NzhjNmFiMTQxEgsSBxDg_8qZjgEYAZIBIgoKcHJvamVjdF9pZBIUQhI0Nzk0NTgxNTYxNzExNjY3MjU&filename=&opi=89354086"},
    {"id": "056f50d9f6604d6ab245b7bc66a8491c", "title": "User Management", "url": "https://contribution.usercontent.google.com/download?c=CgthaWRhX2NvZGVmeBJ6Eh1hcHBfY29tcGFuaW9uX2dlbmVyYXRlZF9maWxlcxpZCiVodG1sX2FmNzFkMjk1MWI1NzQxYzg5MWI3MGNmYTExYjQ5ODc3EgsSBxDg_8qZjgEYAZIBIgoKcHJvamVjdF9pZBIUQhI0Nzk0NTgxNTYxNzExNjY3MjU&filename=&opi=89354086"},
    {"id": "62fde8de8aa34c1cb27cb99d1b4b3be2", "title": "Faculty Management", "url": "https://contribution.usercontent.google.com/download?c=CgthaWRhX2NvZGVmeBJ6Eh1hcHBfY29tcGFuaW9uX2dlbmVyYXRlZF9maWxlcxpZCiVodG1sX2FjYzFhMTgyYjZmMDQ1OTc5NzkwNTY5MjYxMjAzMWU2EgsSBxDg_8qZjgEYAZIBIgoKcHJvamVjdF9pZBIUQhI0Nzk0NTgxNTYxNzExNjY3MjU&filename=&opi=89354086"}
]

output_dir = r"c:\Users\Iyed\Desktop\heysir\stitch_designs"
if not os.path.exists(output_dir):
    os.makedirs(output_dir)

for screen in screens:
    filename = screen["title"].lower().replace(" ", "_") + ".html"
    filepath = os.path.join(output_dir, filename)
    print(f"Downloading {screen['title']} to {filename}...")
    try:
        response = requests.get(screen["url"])
        if response.status_code == 200:
            with open(filepath, "w", encoding="utf-8") as f:
                f.write(response.text)
            print(f"Successfully saved {filename}")
        else:
            print(f"Failed to download {screen['title']}: Status code {response.status_code}")
    except Exception as e:
        print(f"Error downloading {screen['title']}: {e}")
