<template>
  <div style="width: 100%;">
    <el-upload v-if="type == 1" action="#" :file-list="filesList" list-type="picture-card" :auto-upload="false"
      :show-file-list="true" :on-change="handleChange" :on-preview="handlePictureCardPreview" :on-remove="handleRemove"
      :class="{ hide: this.filesList.length >= this.limit }">
      <i class="el-icon-plus"></i>
    </el-upload>
    <el-upload v-else action="#" :file-list="echoList" list-type="picture-card" :auto-upload="false"
      :show-file-list="true" :on-change="handleChangeEcho" :on-preview="handlePictureCardPreviewEcho"
      :on-remove="handleRemoveEcho" :class="{ hide: this.echoList.length >= this.limit }">
      <i class="el-icon-plus"></i>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible" title="预览" width="800" append-to-body :close-on-click-modal="false">
      <img :src="dialogImageUrl" style="display: block; max-width: 100%; margin: 0 auto" />
    </el-dialog>
    <div class="el-upload__tip" slot="tip" v-if="imgWidth>0&&imgHeight>0">
      请上传
      <template > 尺寸为 <b style="color: #f56c6c">{{ imgWidth }}*{{ imgHeight }}</b> </template>
      <!-- <template v-if="fileType"> 格式为 <b style="color: #f56c6c">{{ fileType.join("/") }}</b> </template> -->
      的图片
    </div>
  </div>
</template>

<script>
export default {
  components: {},
  props: {
    value: [String, Object, Array],
    // 图片数量限制
    limit: {
      type: Number,
      default: 5,
    },
    imgWidth: {
      type: Number,
      default: 0,
    },
    imgHeight: {
      type: Number,
      default: 0,
    }
  },
  data() {
    return {
      baseUrl: process.env.VUE_APP_IMAGE_API,
      filesList: [],
      dialogVisible: false,
      dialogImageUrl: '',
      type: 1,
      echoList: []
    };
  },
  computed: {},
  watch: {
    value: {
      handler(val) {
        if (val) {
          if (typeof val === 'string') {
            this.type = 2
            let arr = [{ name:val, url: val }]
            this.echoList = arr
            return arr
          } else {
            this.filesList = val.map(item => {
              if (!item.raw&&!item.uid) {
                item = { name: item.fileUrl, url:  item.fileUrl, id: item.id };
              } else {
                item = item
              }
              return item;
            })
            this.type = 1
            return this.filesList
          }
          // // 首先将值转为数组
          // const list = Array.isArray(val) ? val : this.value.split(',');
          // // 然后将数组转为对象数组
          // this.fileList = list.map(item => {
          //   if (typeof item === "string") {
          //     if (item.indexOf(this.baseUrl) === -1) {
          //         item = { name: this.baseUrl + item, url: this.baseUrl + item };
          //     } else {
          //         item = { name: item, url: item };
          //     }
          //   }
          //   return item;
          // });
        } else {
          this.type = 1
          this.filesList = [];
          return [];
        }
      },
      deep: true,
      immediate: true
    }
  },
  created() {

  },
  mounted() {

  },
  methods: {
    handleRemove(file, filesList) {
      this.filesList = filesList;
      this.$emit("input", this.filesList);
    },
    handlePictureCardPreview(file) {
      // console.log(file,"1212121");
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleChange(file, filesList) {
      // this.limitFileWH(this.imgWidth, this.imgHeight, file)
      // .then(res => {
      //   console.log(res,"121212");
      //   // if (res) {
          this.filesList = filesList;
          this.$emit("input", this.filesList);
      //   // } else {
      //   //   this.handleRemove(null, [])
      //   // }
      // })

    },


    handleRemoveEcho(file, filesList) {
      this.type = 1
      this.echoList = filesList;
      this.$emit("input", this.echoList);
      this.$forceUpdate()

    },
    handlePictureCardPreviewEcho(file) {
      // console.log(file,"1212121");
      this.dialogImageUrl = file&&file.url?file.url:file;
      this.dialogVisible = true;
    },
    handleChangeEcho(file, filesList) {
      this.echoList = filesList;
      // this.$emit("input", this.filesList);
      this.$emit("input", this.echoList);
      this.$forceUpdate()
    },



    // 上传图片事件限制图片尺寸
    limitFileWH(E_width, E_height, file) {
      const _this = this
      let imgWidth = ''
      let imgHight = ''
      const isSize = new Promise(function (resolve, reject) {
        const width = E_width
        const height = E_height
        const _URL = window.URL || window.webkitURL
        const img = new Image()
        img.onload = function () {
          imgWidth = img.width
          imgHight = img.height
          const valid = (img.width === width && img.height === height) || (width == 0 && height == 0)
          valid ? resolve() : reject()
        }
        img.src = _URL.createObjectURL(file)
      }).then(
        () => {
          return true
        },
        () => {
          this.$message.error(
            '上传图片的尺寸应为' +
            E_width +
            '*' +
            E_height +
            '，当前上传图片的尺寸为：' +
            imgWidth +
            '*' +
            imgHight
          )
          return false
        }
      )
      return isSize
    },
  },
}
</script>
<style scoped>

::v-deep.hide .el-upload--picture-card {
  display: none;
}

</style>
