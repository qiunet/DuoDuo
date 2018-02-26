import threading


# 创建全局ThreadLocal对象:
__local = threading.local()


def put(key: str, val):
    """
    往线程变量存放一个key val
    :param key:
    :param val:
    :return:
    """
    __local.__setattr__(key, val)


def get(key: str):
    """
    从线程变量得到数据
    :param key:
    :return:
    """
    return __local.__getattribute__(key)
