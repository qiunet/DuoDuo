#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Comment

__author__ = 'qiunet'
import unittest
from utils import ShellUtil


class TestJsonUtilFunc(unittest.TestCase):
    def testShellExec(self):
        ret = ShellUtil.execShell("df", "-h")
        self.assertEqual(ret, 0)

