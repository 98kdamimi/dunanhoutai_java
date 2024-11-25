import request from '@/utils/request'

// 发现页列表
export function dappList(data) {
    return request({
        url: 'dapp/findList',
        method: 'post',
        data
    })
}

// 编辑发现页
export function dappUpdata(data) {
    return request({
        url: 'dapp/update',
        method: 'post',
        data
    })
}

// 上线发现页
export function dappOnline(id) {
    return request({
        url: 'dapp/online',
        method: 'get',
        params: {
            'id': id
        }
    })
}

//下线发现也
export function dappOffline(id) {
    return request({
        url: 'dapp/Offline',
        method: 'get',
        params: {
            'id': id
        }
    })
}
