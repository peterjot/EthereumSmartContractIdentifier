import os
import solc
import sys
from pathlib import Path

# solc library is only for linux

PATH_HOME = str(Path.home())

COMPILER_BIN = {
    "0.4.22": PATH_HOME + "/.py-solc/solc-v0.4.22/bin/solc",
    "0.4.23": PATH_HOME + "/.py-solc/solc-v0.4.23/bin/solc",
    "0.4.24": PATH_HOME + "/.py-solc/solc-v0.4.24/bin/solc",
    "0.4.25": PATH_HOME + "/.py-solc/solc-v0.4.25/bin/solc"
}

CONTRACT_BLACKLIST = ["SafeMath"]


def main():
    if len(sys.argv) != 2:
        sys.stderr.write("usage: " + sys.argv[0] + " dirpath\n")
        sys.stderr.write("dirpath - path with solidity files")
        sys.stderr.write("every solidity file should endswith with .sol")
        exit(1)

    folder_path = sys.argv[1]
    create_folder(folder_path + "/bin")

    for file_name in os.listdir(folder_path):
        if ".sol" not in file_name:
            continue

        log("\n\n\nCompiling file: " + file_name)

        file_path = folder_path + '/' + file_name
        file_solc_version, _, contract_name, _ = file_name.split("--")

        if file_solc_version is None or contract_name is None:
            sys.stderr.write("Bad file name: " + file_name)
            exit(2)

        bin_file_path = "{}/bin/{}--.bin".format(folder_path, file_name[:-6])
        if os.path.exists(bin_file_path):  # Skip if file is compiled
            log("Skipping. File compiled: " + bin_file_path)
            continue

        if file_solc_version not in COMPILER_BIN.keys():
            sys.stderr.write("File has no supported compiler version: " + file_solc_version)
            exit(2)

        solc_path = COMPILER_BIN[file_solc_version]

        log("Compiler version: " + file_solc_version)
        log("File path: " + file_path)
        log("Set env SOLC_BINARY path to: " + solc_path)
        log("Contract name: " + contract_name)

        os.environ["SOLC_BINARY"] = solc_path

        bin_data = solc.compile_files([file_path], optimize=True)["{}:{}".format(file_path, contract_name)]["bin"]

        log("Saving bin to: " + bin_file_path)
        with open(bin_file_path, "w") as f:
            f.write(bin_data)


def log(msg):
    print("- [LOGGING] - {}".format(msg))


def create_folder(path):
    if not os.path.exists(path):
        os.makedirs(path)


if __name__ == "__main__":
    main()
