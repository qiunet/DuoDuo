#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# 支持byte的工具

__author__ = 'qiunet'

import os
import sys


bytearr = bytearray('中文', 'utf8')
for b in bytearr:
    print(b)

print(len(bytearr))
