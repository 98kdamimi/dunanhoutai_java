// 水印基本设置
let defaultSettings = {
  watermark_id: "wm_div_id", // 水印总体的id
  watermark_prefix: "mask_div_id", // 小水印的id前缀
  watermark_txt: "", // 水印的内容
  watermark_x: 20, // 水印起始位置x轴坐标
  watermark_y: 20, // 水印起始位置Y轴坐标
  watermark_rows: 0, // 水印行数
  watermark_cols: 0, // 水印列数
  watermark_x_space: 50, // 水印x轴间隔
  watermark_y_space: 50, // 水印y轴间隔
  watermark_font: "微软雅黑", // 水印字体
  watermark_color: "black", // 水印字体颜色
  watermark_fontsize: "18px", // 水印字体大小
  watermark_alpha: 0.06, // 水印透明度，要求设置在大于等于0.005
  watermark_width: 100, // 水印宽度
  watermark_height: 100, // 水印长度
  watermark_angle: 15, // 水印倾斜度数
  watermark_parent_width: 0, // 水印的总体宽度（默认值：body的scrollWidth和clientWidth的较大值）
  watermark_parent_height: 0, // 水印的总体高度（默认值：body的scrollHeight和clientHeight的较大值）
  watermark_parent_node: null, // 水印插件挂载的父元素element,不输入则默认挂在body上
  monitor: true, // monitor 是否监控， true: 不可删除水印; false: 可删水印。
};
class WaterMark {
  constructor(options) {
    this.globalSetting = null;
    this.init(options);
  }
  settingsToDefaultSetting(settings) {
    defaultSettings.watermark_id =
      settings.watermark_id || defaultSettings.watermark_id;
    defaultSettings.watermark_prefix =
      settings.watermark_prefix || defaultSettings.watermark_prefix;
    defaultSettings.watermark_txt =
      settings.watermark_txt || defaultSettings.watermark_txt;
    defaultSettings.watermark_x =
      settings.watermark_x || defaultSettings.watermark_x;
    defaultSettings.watermark_y =
      settings.watermark_y || defaultSettings.watermark_y;
    defaultSettings.watermark_rows =
      settings.watermark_rows || defaultSettings.watermark_rows;
    defaultSettings.watermark_cols =
      settings.watermark_cols || defaultSettings.watermark_cols;
    defaultSettings.watermark_x_space =
      settings.watermark_x_space || defaultSettings.watermark_x_space;
    defaultSettings.watermark_y_space =
      settings.watermark_y_space || defaultSettings.watermark_y_space;
    defaultSettings.watermark_font =
      settings.watermark_font || defaultSettings.watermark_font;
    defaultSettings.watermark_color =
      settings.watermark_color || defaultSettings.watermark_color;
    defaultSettings.watermark_fontsize =
      settings.watermark_fontsize || defaultSettings.watermark_fontsize;
    defaultSettings.watermark_alpha =
      settings.watermark_alpha || defaultSettings.watermark_alpha;
    defaultSettings.watermark_width =
      settings.watermark_width || defaultSettings.watermark_width;
    defaultSettings.watermark_height =
      settings.watermark_height || defaultSettings.watermark_height;
    defaultSettings.watermark_angle =
      settings.watermark_angle || defaultSettings.watermark_angle;
    defaultSettings.watermark_parent_width =
      settings.watermark_parent_width || defaultSettings.watermark_parent_width;
    defaultSettings.watermark_parent_height =
      settings.watermark_parent_height ||
      defaultSettings.watermark_parent_height;
    defaultSettings.watermark_parent_node =
      settings.watermark_parent_node || defaultSettings.watermark_parent_node;
    defaultSettings.monitor = settings.monitor || defaultSettings.monitor;
  }
  loadMark(settings) {
    /* 采用配置项替换默认值，作用类似jquery.extend*/
    if (arguments.length === 1 && typeof arguments[0] === "object") {
      var src = arguments[0] || {};
      for (const key in src) {
        if (
          src[key] &&
          defaultSettings[key] &&
          src[key] === defaultSettings[key]
        )
          continue;
        /* veronic: resolution of watermark_angle=0 not in force*/ else if (
          src[key] ||
          src[key] === 0
        )
          defaultSettings[key] = src[key];
      }
    }
    this.settingsToDefaultSetting(settings);
    /* 如果元素存在则移除*/
    var watermark_element = document.getElementById(
      defaultSettings.watermark_id
    );
    watermark_element &&
      watermark_element.parentNode &&
      watermark_element.parentNode.removeChild(watermark_element);
    /* 如果设置水印挂载的父元素的id*/
    var watermark_parent_element = document.getElementById(
      defaultSettings.watermark_parent_node
    );
    var watermark_hook_element = watermark_parent_element || document.body;
    /* 获取页面宽度*/
    var page_width = Math.max(
      watermark_hook_element.scrollWidth,
      watermark_hook_element.clientWidth
    );
    /* 获取页面最大长度*/
    var page_height = Math.max(
      watermark_hook_element.scrollHeight,
      watermark_hook_element.clientHeight
    );
    var setting = arguments[0] || {};
    var parentEle = watermark_hook_element;
    var page_offsetTop = 0;
    var page_offsetLeft = 0;
    if (setting.watermark_parent_width || setting.watermark_parent_height) {
      /* 指定父元素同时指定了宽或高*/
      if (parentEle) {
        page_offsetTop = parentEle.offsetTop || 0;
        page_offsetLeft = parentEle.offsetLeft || 0;
        defaultSettings.watermark_x =
          defaultSettings.watermark_x + page_offsetLeft;
        defaultSettings.watermark_y =
          defaultSettings.watermark_y + page_offsetTop;
      }
    } else {
      if (parentEle) {
        page_offsetTop = parentEle.offsetTop || 0;
        page_offsetLeft = parentEle.offsetLeft || 0;
      }
    }
    /* 创建水印外壳div*/
    var otdiv = document.getElementById(defaultSettings.watermark_id);
    var shadowRoot = null;
    if (!otdiv) {
      otdiv = document.createElement("div");
      /* 创建shadow dom*/
      otdiv.id = defaultSettings.watermark_id;
      // pointer-events: none !important  不会影响原有的DOM操作
      otdiv.setAttribute(
        "style",
        "pointer-events: none !important; display: block !important"
      );
      /* 判断浏览器是否支持attachShadow方法*/
      if (typeof otdiv.attachShadow === "function") {
        /* createShadowRoot Deprecated. Not for use in new websites. Use attachShadow*/
        shadowRoot = otdiv.attachShadow({ mode: "open" });
      } else {
        shadowRoot = otdiv;
      }
      /* 将shadow dom随机插入body内的任意位置*/
      var nodeList = watermark_hook_element.children;
      var index = Math.floor(Math.random() * (nodeList.length - 1));
      if (nodeList[index]) {
        watermark_hook_element.insertBefore(otdiv, nodeList[index]);
      } else {
        watermark_hook_element.appendChild(otdiv);
      }
    } else if (otdiv.shadowRoot) {
      shadowRoot = otdiv.shadowRoot;
    }
    /* 三种情况下会重新计算水印列数和x方向水印间隔：1、水印列数设置为0，2、水印宽度大于页面宽度，3、水印宽度小于于页面宽度*/
    defaultSettings.watermark_cols = parseInt(
      (page_width - defaultSettings.watermark_x) /
        (defaultSettings.watermark_width + defaultSettings.watermark_x_space) ||
        0
    );
    var temp_watermark_x_space = parseInt(
      (page_width -
        defaultSettings.watermark_x -
        defaultSettings.watermark_width * defaultSettings.watermark_cols) /
        defaultSettings.watermark_cols
    );
    defaultSettings.watermark_x_space = temp_watermark_x_space
      ? defaultSettings.watermark_x_space
      : temp_watermark_x_space;
    var allWatermarkWidth;
    /* 三种情况下会重新计算水印行数和y方向水印间隔：1、水印行数设置为0，2、水印长度大于页面长度，3、水印长度小于于页面长度*/
    defaultSettings.watermark_rows = parseInt(
      (page_height - defaultSettings.watermark_y) /
        (defaultSettings.watermark_height +
          defaultSettings.watermark_y_space) || 0
    );
    var temp_watermark_y_space = parseInt(
      (page_height -
        defaultSettings.watermark_y -
        defaultSettings.watermark_height * defaultSettings.watermark_rows) /
        defaultSettings.watermark_rows
    );
    defaultSettings.watermark_y_space = temp_watermark_y_space
      ? defaultSettings.watermark_y_space
      : temp_watermark_y_space;
    var allWatermarkHeight;
    if (watermark_parent_element) {
      allWatermarkWidth =
        defaultSettings.watermark_x +
        defaultSettings.watermark_width * defaultSettings.watermark_cols +
        defaultSettings.watermark_x_space *
          (defaultSettings.watermark_cols - 1);
      allWatermarkHeight =
        defaultSettings.watermark_y +
        defaultSettings.watermark_height * defaultSettings.watermark_rows +
        defaultSettings.watermark_y_space *
          (defaultSettings.watermark_rows - 1);
    } else {
      allWatermarkWidth =
        page_offsetLeft +
        defaultSettings.watermark_x +
        defaultSettings.watermark_width * defaultSettings.watermark_cols +
        defaultSettings.watermark_x_space *
          (defaultSettings.watermark_cols - 1);
      allWatermarkHeight =
        page_offsetTop +
        defaultSettings.watermark_y +
        defaultSettings.watermark_height * defaultSettings.watermark_rows +
        defaultSettings.watermark_y_space *
          (defaultSettings.watermark_rows - 1);
    }
    var x;
    var y;
    for (var i = 0; i < defaultSettings.watermark_rows; i++) {
      if (watermark_parent_element) {
        y =
          page_offsetTop +
          defaultSettings.watermark_y +
          (page_height - allWatermarkHeight) / 2 +
          (defaultSettings.watermark_y_space +
            defaultSettings.watermark_height) *
            i;
      } else {
        y =
          defaultSettings.watermark_y +
          (page_height - allWatermarkHeight) / 2 +
          (defaultSettings.watermark_y_space +
            defaultSettings.watermark_height) *
            i;
      }
      for (var j = 0; j < defaultSettings.watermark_cols; j++) {
        if (watermark_parent_element) {
          x =
            page_offsetLeft +
            defaultSettings.watermark_x +
            (page_width - allWatermarkWidth) / 2 +
            (defaultSettings.watermark_width +
              defaultSettings.watermark_x_space) *
              j;
        } else {
          x =
            defaultSettings.watermark_x +
            (page_width - allWatermarkWidth) / 2 +
            (defaultSettings.watermark_width +
              defaultSettings.watermark_x_space) *
              j;
        }
        var mask_div = document.createElement("div");
        var oText = document.createTextNode(defaultSettings.watermark_txt);
        mask_div.appendChild(oText);
        /* 设置水印相关属性start*/
        mask_div.id = defaultSettings.watermark_prefix + i + j;
        /* 设置水印div倾斜显示*/
        mask_div.style.webkitTransform =
          "rotate(-" + defaultSettings.watermark_angle + "deg)";
        mask_div.style.MozTransform =
          "rotate(-" + defaultSettings.watermark_angle + "deg)";
        mask_div.style.msTransform =
          "rotate(-" + defaultSettings.watermark_angle + "deg)";
        mask_div.style.OTransform =
          "rotate(-" + defaultSettings.watermark_angle + "deg)";
        mask_div.style.transform =
          "rotate(-" + defaultSettings.watermark_angle + "deg)";
        mask_div.style.visibility = "";
        mask_div.style.position = "absolute";
        /* 选不中*/
        mask_div.style.left = x + "px";
        mask_div.style.top = y + "px";
        mask_div.style.overflow = "hidden";
        mask_div.style.zIndex = "9999999";
        mask_div.style.opacity = defaultSettings.watermark_alpha;
        mask_div.style.fontSize = defaultSettings.watermark_fontsize;
        mask_div.style.fontFamily = defaultSettings.watermark_font;
        mask_div.style.color = defaultSettings.watermark_color;
        mask_div.style.textAlign = "center";
        mask_div.style.width = defaultSettings.watermark_width + "px";
        mask_div.style.height = defaultSettings.watermark_height + "px";
        mask_div.style.display = "block";
        mask_div.style["-ms-user-select"] = "none";
        /* 设置水印相关属性end*/
        shadowRoot.appendChild(mask_div);
      }
    }
    this.observerFunc(settings, watermark_hook_element);
  }
  observerFunc(settings, watermark_hook_element) {
    // monitor 是否监控， true: 不可删除水印; false: 可删水印。
    // 监听dom是否被移除或者改变属性的回调函数
    var callback = (records) => {
      if (
        (this.globalSetting && records.length === 1) ||
        (records.length === 1 && records[0].removedNodes.length >= 1)
      ) {
        this.loadMark(this.globalSetting);
      }
    };
    const MutationObserver =
      window.MutationObserver ||
      window.WebKitMutationObserver ||
      window.MozMutationObserver;
    var watermarkDom = new MutationObserver(callback);
    var option = {
      childList: true,
      attributes: true,
      subtree: true,
    };
    const minotor =
      settings.monitor === undefined
        ? defaultSettings.monitor
        : settings.monitor;
    if (minotor) {
      watermarkDom.observe(watermark_hook_element, option);
      watermarkDom.observe(
        document.getElementById("wm_div_id").shadowRoot,
        option
      );
    } else {
    }
  }
  removeMark() {
    /* 移除水印*/
    /* 采用配置项替换默认值，作用类似jquery.extend*/
    if (arguments.length === 1 && typeof arguments[0] === "object") {
      var src = arguments[0] || {};
      for (const key in src) {
        if (
          src[key] &&
          defaultSettings[key] &&
          src[key] === defaultSettings[key]
        )
          continue;
        /* veronic: resolution of watermark_angle=0 not in force*/ else if (
          src[key] ||
          src[key] === 0
        )
          defaultSettings[key] = src[key];
      }
    }
    /* 移除水印*/
    var watermark_element = document.getElementById(
      defaultSettings.watermark_id
    );
    var _parentElement = watermark_element.parentNode;
    _parentElement.removeChild(watermark_element);
  }
  init(settings) {
    this.globalSetting = settings;
    this.loadMark(settings);
    window.addEventListener("onload", () => {
      this.loadMark(settings);
    });
    window.addEventListener("resize", () => {
      this.loadMark(settings);
    });
  }
  load(settings) {
    /* 手动加载水印*/
    this.globalSetting = settings;
    this.loadMark(settings);
  }
  remove() {
    /* 手动移除水印*/
    this.removeMark();
  }
}
export default WaterMark;