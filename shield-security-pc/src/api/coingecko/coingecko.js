import request from '@/utils/request'

// 网络列表下代币列表
export function findCoingeckoToken(data) {
    return request({
        url: 'coingecko/findCoingeckoToken',
        method: 'post',
        data
    })
}

export function findCoingeckoTokenByNetWorkId(id) {
    return request({
        url: 'coingecko/findCoingeckoTokenByNetWorkId',
        method: 'get',
        params: {
            'id': id
        }
    })
}

