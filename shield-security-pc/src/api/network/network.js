import request from '@/utils/request'

// 网络列表
export function networkList(data) {
    return request({
        url: 'network/findList',
        method: 'post',
        data
    })
}

//rpc网络列表
export function networkObtainList() {
    return request({
        url: 'network/obtainList',
        method: 'get',
    })
}

//格式化网络列表
export function networkAll() {
    return request({
        url: 'network/findAll',
        method: 'get',
    })
}