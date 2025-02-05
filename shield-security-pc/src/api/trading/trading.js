import request from '@/utils/request'

// 列表
export function tradingLableList() {
    return request({
        url: 'tradingLableState/getList',
        method: 'get',
    })
}

//编辑
export function tradingLableUpdate(id,states) {
    return request({
        url: 'tradingLableState/update',
        method: 'get',
        params: {
            'id': id,
            'states':states
        }
    })
}

