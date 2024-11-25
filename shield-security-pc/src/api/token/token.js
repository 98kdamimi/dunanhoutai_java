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
export function findThirdParty(data) {
    return request({
        url: 'token/ThirdPartylist',
        method: 'get',
    })
}