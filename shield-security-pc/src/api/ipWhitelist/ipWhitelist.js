import request from '@/utils/request'

//ip白名单列表
export function ipWhitelistList(data) {
    return request({
        url: 'ipWhitelist/getList',
        method: 'post',
        data
    })
}


// 新增
export function ipWhitelistAdd(data) {
    return request({
        url: 'ipWhitelist/add',
        method: 'post',
        data: data
    })
}


export function ipWhitelistDelete(id) {
    return request({
        url: 'ipWhitelist/delete',
        method: 'get',
        params: {
            'id': id
        }
    })
}
