import request from '@/utils/request'

// 代币列表
export function tokenList(data) {
    return request({
        url: 'token/findList',
        method: 'post',
        data
    })
}

//第三方平台代币列表
export function ThirdPartyList(data) {
    return request({
        url: 'token/findListThirdParty',
        method: 'post',
        data
    })
}

//获取第三方平台代币
export function findThirdParty() {
    return request({
        url: 'token/ThirdPartylist',
        method: 'get',
    })
}

export function findTokenSource() {
    return request({
        url: 'token/findSource',
        method: 'get',
    })
}

export function tokenUpdata(data) {
    return request({
        url: 'token/update',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data' //注意这里
        },
        data: data
    })
}

export function tokenAdd(data) {
    return request({
        url: 'token/add',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data' //注意这里
        },
        data: data
    })
}