import request from '@/utils/request'

// 新增联系方式
export function abbreviationAdd(data) {
    return request({
        url: 'abbreviation/add',
        method: 'post',
        data
    })
}

//编辑联系方式
export function abbreviationUpdate(data) {
    return request({
        url: 'abbreviation/update',
        method: 'post',
        data
    })
}

//联系方式列表
export function abbreviationlist(data) {
    return request({
        url: 'abbreviation/getList',
        method: 'post',
        data
    })
}

export function abbreviationDelete(id) {
    return request({
        url: 'abbreviation/delete',
        method: 'get',
        params: {
            'id': id
        }
    })
}
