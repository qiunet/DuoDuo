import sys
import pandas as pd

"""
Usage:

python3 ConvertLogRecord.py log1 log2 "xlsx FilePath"

pip freeze > requirements.txt create  requirements.txt

you need execute command "pip install -r  requirements.txt" first
"""


class LogParam:

    def __init__(self):
        self.header_mapping = {}
        self.header_names = []
        self.data = []

    def header_mapping_index(self, name) -> int:
        if self.header_mapping.get(name) is None:
            self.header_mapping[name] = len(self.header_names)
            self.header_names.append(name)
            return len(self.header_names) - 1
        return self.header_mapping[name]

    def get_header_names(self):
        return self.header_names

    def add_data(self, data):
        self.data.append(data)

    def get_data(self):
        return self.data


def read_log_file(params, file):
    print(f"==Handler file[{file}]")
    with open(file) as log:
        for line in log.readlines():
            content = line.strip()
            r_content = content[26::].split("|")
            arr = ["null"] * (len(r_content) + 1)
            arr[params.header_mapping_index("dt")] = content[1:24]
            for string in r_content:
                strs = string.split("=")
                idx = params.header_mapping_index(strs[0])
                arr[idx] = strs[1]
            params.get_data().append(arr)


if __name__ == '__main__':
    param = LogParam()
    # 1:: 表示1 之后的, 1: -1 表示1 到倒数第二个.
    for index, file_path in enumerate(sys.argv[1:-1]):
        read_log_file(param, file_path)

    with pd.ExcelWriter(sys.argv[-1], mode="w", engine="openpyxl") as writer:
        df = pd.DataFrame(param.get_data(), columns=param.get_header_names())
        df.to_excel(writer, sheet_name="数据")
