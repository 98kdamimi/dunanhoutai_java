import request from '@/utils/request'

export function addTronWebconfig(data) {
    return request({
        url: 'tronWebconfig/add',
        method: 'post',
        data
    })
}

export function findTronWebconfig() {
    return request({
        url: 'tronWebconfig/find',
        method: 'get',
    })
}

export function deleteTronWebconfig(id) {
    return request({
        url: 'tronWebconfig/delete',
        method: 'get',
        params: {
            'id': id
        }
    })
}

