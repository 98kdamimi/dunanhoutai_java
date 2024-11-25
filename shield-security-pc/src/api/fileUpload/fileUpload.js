import request from '@/utils/request'

// 资源列表
export function uploadList(data) {
    return request({
        url: 'upload/findList',
        method: 'post',
        data
    })
}

// 资源分类列表
export function uploadTypeList() {
    return request({
        url: 'upload/findTypeList',
        method: 'get',
        
    })
}

// 资源分类列表
export function fileUpload(data) {
    return request({
        url: 'upload/fileUpload',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data' //注意这里
        },
        data: data
    })
}

