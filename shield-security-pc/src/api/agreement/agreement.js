import request from '@/utils/request'

// 新增协议
export function agreementAdd(data) {
    return request({
        url: 'agreement/add',
        method: 'post',
        data
    })
}

//协议列表
export function agreementFindType(typeId) {
    return request({
        url: 'agreement/findType',
        method: 'post',
        params: {
            'typeId': typeId
        }
    })
}

export function agreementUpdate(data) {
    return request({
        url: 'agreement/update',
        method: 'post',
        data
    })
}

export function agreementDelete(id) {
    return request({
        url: 'agreement/delete',
        method: 'post',
        params: {
            'id': id
        }
    })
}