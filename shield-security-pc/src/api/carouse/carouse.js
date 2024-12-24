import request from '@/utils/request'

//轮播图列表
export function carouseGetList(data) {
    return request({
        url: 'carouse/getList',
        method: 'post',
        data
        // params: {
        //     'language': language
        // }
    })
}


// 新增轮播图
export function carouseAdd(data) {
    return request({
        url: 'carouse/add',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data' //注意这里
        },
        data: data
    })
}

// 编辑轮播图
export function carouseUpdate(data) {
    return request({
        url: 'carouse/update',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data' //注意这里
        },
        data: data
    })
}

export function carouseDelete(id) {
    return request({
        url: 'carouse/delete',
        method: 'get',
        params: {
            'id': id
        }
    })
}
