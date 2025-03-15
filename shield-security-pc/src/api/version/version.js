import request from '@/utils/request'

// 版本管理列表
export function versionList(data) {
    return request({
        url: 'version/softwareList',
        method: 'post',
        data
    })
}

// 上线
export function versionOnline(id,forceUpdateLable) {
    return request({
        url: 'version/onlineSoftware',
        method: 'get',
        params: {
            'id': id,
            "forceUpdateLable":forceUpdateLable
        }
    })
}

export function versionOffline(id) {
    return request({
        url: 'version/Offline',
        method: 'post',
        params: {
            'id': id
        }
    })
}

//硬件列表
export function hardwareFindList(data) {
    return request({
        url: 'version/hardwareList',
        method: 'post',
        data
    })
}

// 硬件上线
export function onlineHardware(id) {
    return request({
        url: 'version/onlineHardware',
        method: 'get',
        params: {
            'id': id
        }
    })
}

//新消息 
export function msgWarn() {
    return request({
        url: 'version/msgWarn',
        method: 'get',
    })
}

//删除消息
export function deleteMsg() {
    return request({
        url: 'version/deleteMsg',
        method: 'get',
    })
}

// 软件版本发布
export function releaseVersion(data) {
    return request({
        url: 'version/sysAdd',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data' //注意这里
        },
        data: data,
        timeout: 50000  // 设置超时时间为 5000ms（5秒）
    })
}

//硬件版本发布
export function releaseHardwareVersion(data) {
    return request({
        url: 'version/sysHardwareAdd',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data' //注意这里
        },
        data: data,
        timeout: 50000  // 设置超时时间为 5000ms（5秒）
    })
}