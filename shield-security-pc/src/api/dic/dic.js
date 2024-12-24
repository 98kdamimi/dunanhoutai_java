import request from '@/utils/request'

// 查询语言列表
export function getLanguage() {
    return request({
        url: 'dic/getLanguage',
        method: 'get',
    })
}