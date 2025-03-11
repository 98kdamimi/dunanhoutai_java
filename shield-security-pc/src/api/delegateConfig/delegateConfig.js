import request from '@/utils/request'

export function addDelegateConfig(data) {
    return request({
        url: 'delegateConfig/add',
        method: 'post',
        data
    })
}

export function findDelegateConfig() {
    return request({
        url: 'delegateConfig/find',
        method: 'get',
    })
}

export function deleteDelegateConfig(id) {
    return request({
        url: 'delegateConfig/delete',
        method: 'get',
        params: {
            'id': id
        }
    })
}

