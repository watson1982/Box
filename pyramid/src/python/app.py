import os
import requests
from importlib.machinery import SourceFileLoader
import json


def spider(cache, key, api):
    name = os.path.basename(api)
    path = cache + '/' + name
    downloadFile(path, api)
    return SourceFileLoader(name, path).load_module().Spider()


def downloadFile(name, api):
    if api.startswith('http'):
        r = redirect(api)
        if r.status_code == 200:
            # writeFile(name, redirect(api).content)
            writeFile(name, r.content)
            return True
        else:
            return False
    else:
        writeFile(name, str.encode(api))
        return True


def writeFile(name, content):
    with open(name, 'wb') as f:
        f.write(content)


def redirect(url):
    rsp = requests.get(url, allow_redirects=False, verify=False)
    if 'Location' in rsp.headers:
        return redirect(rsp.headers['Location'])
    else:
        return rsp


def createFile(file_path):
    if os.path.exists(file_path) is False:
        os.makedirs(file_path)


def downloadPlugin(basePath, url):
    createFile(basePath)
    name = url.split('/')[-1].split('.')[0]
    pyName = ''
    if url.startswith('file://'):
        pyName = url.replace('file://', '')
    else:
        pyName = basePath + name + '.py'
        downloadFile(pyName, url)
    sPath = gParam['SpiderPath']
    sPath[name] = pyName
    # sParam = gParam['SpiderParam']
    # paramList = parse.parse_qs(parse.urlparse(url).query).get('extend')
    # if paramList == None:
    #     paramList = ['']
    # sParam[name] = paramList[0]
    return pyName


def loadFromDisk(fileName):
    name = fileName.split('/')[-1].split('.')[0]
    spList = gParam['SpiderList']
    if name not in spList:
        sp = SourceFileLoader(name, fileName).load_module().Spider()
        spList[name] = sp
    return spList[name]


def str2json(content):
    return json.loads(content)


gParam = {
    "SpiderList": {},
    "SpiderPath": {},
    # "SpiderParam": {}
}


def getDependence(ru):
    result = ru.getDependence()
    return result


def init(ru, extend):
    spoList = []
    spList = gParam['SpiderList']
    sPath = gParam['SpiderPath']
    # sParam = gParam['SpiderParam']
    for key in ru.getDependence():
        sp = None
        if key in spList.keys():
            sp = spList[key]
        elif key in sPath.keys():
            sp = loadFromDisk(sPath[key])
        if sp != None:
            # sp.setExtendInfo(sParam[key])
            spoList.append(sp)
    ru.setExtendInfo(extend)
    ru.init(spoList)


def homeContent(ru, filter):
    result = ru.homeContent(filter)
    formatJo = json.dumps(result, ensure_ascii=False)
    return formatJo


def homeVideoContent(ru):
    result = ru.homeVideoContent()
    formatJo = json.dumps(result, ensure_ascii=False)
    return formatJo


def categoryContent(ru, tid, pg, filter, extend):
    result = ru.categoryContent(tid, pg, filter, str2json(extend))
    formatJo = json.dumps(result, ensure_ascii=False)
    return formatJo


def detailContent(ru, array):
    result = ru.detailContent(str2json(array))
    formatJo = json.dumps(result, ensure_ascii=False)
    return formatJo


def playerContent(ru, flag, id, vipFlags):
    result = ru.playerContent(flag, id, str2json(vipFlags))
    formatJo = json.dumps(result, ensure_ascii=False)
    return formatJo


def searchContent(ru, key, quick):
    result = ru.searchContent(key, quick)
    formatJo = json.dumps(result, ensure_ascii=False)
    return formatJo


def searchContentPage(ru, key, quick, pg):
    result = ru.searchContentPage(key, quick, pg)
    formatJo = json.dumps(result, ensure_ascii=False)
    return formatJo


def localProxy(ru, param):
    result = ru.localProxy(str2json(param))
    return result


def run():
    pass


if __name__ == '__main__':
    run()
