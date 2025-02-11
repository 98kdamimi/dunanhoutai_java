import request from '@/utils/request'


//交易流水记录2
export function transactionlist(data) {
    return request({
        url: 'transaction/findList',
        method: 'post',
        data
    })
}