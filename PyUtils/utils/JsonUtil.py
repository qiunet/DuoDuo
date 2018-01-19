# -*- coding: utf-8 -*-

# Json 工具类

import json

__author__ = 'qiunet'


def toJsonString(obj):
    return json.dumps(obj)


def getGeneralObject(jsonString, cls=None):
    return json.loads(jsonString, encoding="UTF8", cls=cls)

