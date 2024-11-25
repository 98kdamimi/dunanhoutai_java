import request from '@/utils/request'

// 证书列表
export function certificateList(data) {
    return request({
        url: 'certificate/findList',
        method: 'post',
        data
    })
}

//上传证书
export function certificateUpload(data) {
    return request({
        url: 'certificate/upload',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data' //注意这里
        },
        data: data
    })
}

export function certificateDownload(id) {
    return request({
        url: 'certificate/downloadFile',
        method: 'get',
        responseType: 'text',
        params: {
            'id': id
        }
    })
}