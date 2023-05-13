#!/usr/bin/env python3

import requests
import gzip

#
# Generates the manifest for the 'SingleFileStateMachineReader' usecase that uses the CommonCrawlWARCSingleFileStateMachineReader implementation class
#
# Instructions:
# -------------
# 1. Replace the warc.paths.gz url below with the URL for the dataset you want to process. For example, the Nov/Dec dataset is at https://commoncrawl.org/2022/12/nov-dec-2022-crawl-archive-now-available/
# 2. Update the '#!/usr/bin/env python3' import line as appropriate for the environment
# 3. Install the requests library if its not already installed - https://requests.readthedocs.io/en/latest/user/install/#install
# 4. The script generates the data for all WARC files in the dataset. This is okay if you are mining data for production usecases. For testing and other such uses, we recommend using a subset of files ~ 10 or so. (Keep the first 10 lines in the generated file)
with requests.get("https://data.commoncrawl.org/crawl-data/CC-MAIN-2022-27/warc.paths.gz",stream=True) as res:
    extracted = gzip.decompress(res.content)
    for line in extracted.split(b'\n'):
        line = line.decode()
        if line != "":
            print (line)