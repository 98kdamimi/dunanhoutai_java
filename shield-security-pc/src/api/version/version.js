import request from '@/utils/request'

// 版本管理列表
export function versionList(data) {
    return request({
        url: 'version/findList',
        method: 'post',
        data
    })
}

// 上线
export function versionOnline(id) {
    return request({
        url: 'version/onlineSoftware',
        method: 'post',
        params: {
            'id': id
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
        url: 'version/hardwareFindList',
        method: 'post',
        data
    })
}

// 硬件上线
export function onlineHardware(id) {
    return request({
        url: 'version/onlineHardware',
        method: 'post',
        params: {
            'id': id
        }
    })
}