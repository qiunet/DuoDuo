# -*- coding: utf-8 -*-

# Json 工具类

import json

__author__ = 'qiunet'


def toJsonString(obj: object) -> str:
    return json.dumps(obj, default=lambda obj: obj.__dict__)


def getGeneralObject(jsonString: str):
    return json.loads(jsonString)

