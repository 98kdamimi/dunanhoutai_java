var now = new Date(); // 当前日期
var nowDayOfWeek = now.getDay(); // 今天本周的第几天
var nowDay = now.getDate(); // 当前日
var nowMonth = now.getMonth(); // 当前月
var nowYear = now.getYear(); // 当前年
nowYear += (nowYear < 2000) ? 1900 : 0;

var DateUtil = {
    /**
     * 获得当前日期
     *
     * @returns
     */
    getNowDay() {
        return this.formatDate(new Date());
    },

    /**
     * 获取当前月份
     */
    getYearMonth() {
        const currentDate = new Date();
        const year = currentDate.getFullYear();
        const month = String(currentDate.getMonth() + 1).padStart(2, '0');
        return year + "-" + month
    },

    formattedLastMonth() {
        const currentDate = new Date();
        const lastMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() - 1);
        const year = lastMonth.getFullYear();
        const month = String(lastMonth.getMonth() + 1).padStart(2, '0');
        return year + "-" + month
    },

    /**
     * 获取当前时间
     */
    getNowDateTime() {
        return this.formatTime(new Date());
    },
    /**
     * 获得本周的开始时间
     *
     * @returns
     */
    getStartDayOfWeek() {
        var day = nowDayOfWeek || 7;
        return this.formatDate(new Date(now.getFullYear(), nowMonth, nowDay + 1 - day));
    },
    /**
     * 获得本周的结束时间
     *
     * @returns
     */
    getEndDayOfWeek() {
        var day = nowDayOfWeek || 7;
        return this.formatDate(new Date(now.getFullYear(), nowMonth, nowDay + 7 - day));
    },
    /**
     * 获得本月的开始时间
     *
     * @returns
     */
    getStartDayOfMonth() {
        var monthStartDate = new Date(nowYear, nowMonth, 1);
        return this.formatDate(monthStartDate);
    },
    /**
     * 获得本月的结束时间
     *
     * @returns
     */
    getEndDayOfMonth() {
        var monthEndDate = new Date(nowYear, nowMonth, this.getMonthDays());
        return this.formatDate(monthEndDate);
    },
    /**
     * 获得本年的开始时间和结束时间
     *
     * @returns
     */
    getYear(type, dates) {
        var dd = new Date();
        var n = dates || 0;
        var year = dd.getFullYear() + Number(n);
        if (type == "s") {
            var day = year + "-01-01";
        };
        if (type == "e") {
            var day = year + "-12-31";
        };
        if (!type) {
            var day = year + "-01-01/" + year + "-12-31";
        };
        return day;
    },
    getYearJustMonth(type, dates) {
        var dd = new Date();
        var n = dates || 0;
        var year = dd.getFullYear() + Number(n);
        if (type == "s") {
            var day = year + "-01";
        };
        if (type == "e") {
            var day = year + "-12";
        };
        return day;
    },
    /**
     * 获得本月天数
     *
     * @returns
     */
    getMonthDays() {
        var monthStartDate = new Date(nowYear, nowMonth, 1);
        var monthEndDate = new Date(nowYear, nowMonth + 1, 1);
        var days = (monthEndDate - monthStartDate) / (1000 * 60 * 60 * 24);
        return days;
    },
    getMDays(y, m) {
        if (m < 10) { m = m.substr(1, 1) }
        var date = new Date();
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        let days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
        if ((y % 4 === 0) && (y % 100 !== 0 || y % 400 === 0)) {
            days[1] = 29
        }
        return days[m - 1]
    },
    /**
     * @param 日期格式化
     * @returns {String}
     */
    formatDate(date) {
        var myyear = date.getFullYear();
        var mymonth = date.getMonth() + 1;
        var myweekday = date.getDate();

        if (mymonth < 10) {
            mymonth = "0" + mymonth;
        }
        if (myweekday < 10) {
            myweekday = "0" + myweekday;
        }
        return (myyear + "-" + mymonth + "-" + myweekday);
    },
    //上月日期
    getLastMonth() {
        var year, lastMonth;
        var date = new Date();
        var nowYear = date.getFullYear(); //当前年：四位数字
        var nowMonth = date.getMonth(); //当前月：0-11
        if (nowMonth == 0) {
            //如果是0，则说明是1月份，上一个月就是去年的12月
            year = nowYear - 1;
            lastMonth = 12;
        } else {
            //不是1月份，年份为当前年，月份本来是要减1的，但是由于`getMonth()`的月份本身就是少了1的，所以月份不用变。
            year = nowYear;
            lastMonth = nowMonth;
        }
        lastMonth = lastMonth < 10 ? "0" + lastMonth : lastMonth; //月份格式化：月份小于10则追加个0
        let lastYearMonth = year + "-" + lastMonth;
        var lastDay = new Date(year, lastMonth, 0).getDate(); //获取当月最后一日
        lastDay = lastDay < 10 ? "0" + lastDay : lastDay; //日数补 0

        return {
            startTime: lastYearMonth + "-01",
            endTime: lastYearMonth + "-" + lastDay,
        };
    },
    //时间格式化
    // formatDate(row, column) {
    //   // 获取单元格数据
    //   let data = row[column.property]
    //   if (data == null) {
    //     return null
    //   }
    //   let dt = new Date(data)
    //   return dt.getFullYear() + '-' + (dt.getMonth() + 1) + '-' + dt.getDate() + ' ' + dt.getHours() + ':' + dt
    //     .getMinutes() + ':' + dt.getSeconds()
    // },
    format(val) {
        var date = new Date(Number(val)); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
        var Y = date.getFullYear() + "-";
        var M = (date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1) + "-";
        var D = date.getDate() + " ";
        var h = date.getHours() + ":";
        var m = date.getMinutes() + ":";
        var s = (date.getSeconds() < 10 ? "0" + (date.getSeconds()) : date.getSeconds());
        return Y + M + D + h + m + s;
    },

    formatTime(val) {
        var date = new Date(Number(val)); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
        var Y = date.getFullYear() + "-";
        var M = (date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1) + "-";
        var D = date.getDate() + "/";
        var h = date.getHours() + ":";
        var m = date.getMinutes() + ":";
        var s = date.getSeconds();
        return Y + M + D + h + m + s;
    },

    // 标准时间转换成中国标准时间 如： 2023-04-21  -   Fri Apr 21 2023 00:00:00 GMT+0800 (中国标准时间)
    formatterDate(date) {
        let result = new Date(date);
        return result;
    },

    // 获取前天 明天 后天 大后天等的日期...
    // 传入 -1 就是昨天日期，传入 1 就是明天日期
    GetDateStr(AddDayCount) {
        var dd = new Date();
        dd.setDate(dd.getDate() + AddDayCount); //获取AddDayCount天后的日期
        var y = dd.getFullYear();
        var m = dd.getMonth() + 1 < 10 ? "0" + (dd.getMonth() + 1) : dd.getMonth() + 1;
        var d = dd.getDate() < 10 ? "0" + dd.getDate() : dd.getDate();
        return y + "-" + m + "-" + d;
    },
    //指定时间前后几天的日期
    // getNextDate(date, day) {
    //   console.log(date, day)
    //   var dd = new Date(date);
    //   dd.setDate(dd.getDate() + day);
    //   var y = dd.getFullYear();
    //   var m = dd.getMonth() + 1 < 10 ? "0" + (dd.getMonth() + 1) : dd.getMonth() + 1;
    //   var d = dd.getDate() < 10 ? "0" + dd.getDate() : dd.getDate();
    //   return y + "-" + m + "-" + d;
    // },
    // 计算两个日期间的天数
    getDifferTime(startDate, endDate) {
        let startTime = new Date(Date.parse(startDate.replace(/-/g, "/"))).getTime();
        let endTime = new Date(Date.parse(endDate.replace(/-/g, "/"))).getTime();
        let dates = Math.abs((startTime - endTime)) / (1000 * 60 * 60 * 24);
        return dates;
    },

    getDiffDay(date_1, date_2) {
        // 计算两个日期之间的差值
        let totalDays, diffDate
        let myDate_1 = Date.parse(date_1)
        let myDate_2 = Date.parse(date_2)
            // 将两个日期都转换为毫秒格式，然后做差
        diffDate = Math.abs(myDate_1 - myDate_2) // 取相差毫秒数的绝对值
        totalDays = Math.floor(diffDate / (1000 * 3600 * 24)) // 向下取整
            // console.log(totalDays)
        return totalDays // 相差的天数
    },

    // 查询当天日期(年-月-日)
    getNowDate() {
        const timeOne = new Date();
        const year = timeOne.getFullYear();
        let month = timeOne.getMonth() + 1;
        let day = timeOne.getDate();
        month = month < 10 ? "0" + month : month;
        day = day < 10 ? "0" + day : day;
        const NOW_MONTHS_AGO = `${year}-${month}-${day}`;
        return NOW_MONTHS_AGO;
    },
    //指定时间前后几天的日期
    getDay(beginDate, days) {
        var beginDate = beginDate.split("-");
        var nDate = new Date(beginDate[1] + '-' + beginDate[2] + '-' + beginDate[0]); //转换为MM-DD-YYYY格式
        var millSeconds = Math.abs(nDate) + (days * 24 * 60 * 60 * 1000);
        var rDate = new Date(millSeconds);
        var year = rDate.getFullYear();
        var month = rDate.getMonth() + 1;
        if (month < 10) month = "0" + month;
        var date = rDate.getDate();
        if (date < 10) date = "0" + date;
        return (year + "-" + month + "-" + date);
    },
    //结束时间到目前时间的天数
    DateDiff(sDate1, sDate2) {
        var oDate1, oDate2, iDays
        oDate1 = new Date(sDate1.substring(4, 6) + '-' + sDate1.substring(6) + '-' + sDate1.substring(0, 4));
        oDate2 = new Date(sDate2.substring(4, 6) + '-' + sDate2.substring(6) + '-' + sDate2.substring(0, 4));
        iDays = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 / 24);
        return iDays;
    },

    // 计算指定日期加上多少天，加多少月，加多少年的日期
    DateAdd(interval, number, date) {
        /*
         *   功能:实现VBScript的DateAdd功能.
         *   参数:interval,字符串表达式，表示要添加的时间间隔.
         *   参数:number,数值表达式，表示要添加的时间间隔的个数.
         *   参数:date,时间对象.
         *   返回:新的时间对象.
         *   var   now   =   new   Date();
         *   var   newDate   =   DateAdd( "d ",5,now); 加5天
         *---------------   DateAdd(interval,number,date)   -----------------
         */
        switch (interval) {
            case "y ":
                {
                    date.setFullYear(date.getFullYear() + number);
                    return date;
                    break;
                }
            case "q ":
                {
                    date.setMonth(date.getMonth() + number * 3);
                    return date;
                    break;
                }
            case "m ":
                {
                    date.setMonth(date.getMonth() + number);
                    return date;
                    break;
                }
            case "w ":
                {
                    date.setDate(date.getDate() + number * 7);
                    return date;
                    break;
                }
            case "d ":
                {
                    date.setDate(date.getDate() + number);
                    return date;
                    break;
                }
            case "h ":
                {
                    date.setHours(date.getHours() + number);
                    return date;
                    break;
                }
            case "m ":
                {
                    date.setMinutes(date.getMinutes() + number);
                    return date;
                    break;
                }
            case "s ":
                {
                    date.setSeconds(date.getSeconds() + number);
                    return date;
                    break;
                }
            default:
                {
                    date.setDate(d.getDate() + number);
                    return date;
                    break;
                }
        }
    }



};

export default DateUtil