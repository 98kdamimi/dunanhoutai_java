import request from '@/utils/request'

// 新增联系方式
export function walletBalanceList(data) {
    return request({
        url: 'walletBalance/getList',
        method: 'post',
        data
    })
}
