import json

file_path = 'list.json'

with open(file_path, 'r') as file:
    data = json.load(file)

print(data)
