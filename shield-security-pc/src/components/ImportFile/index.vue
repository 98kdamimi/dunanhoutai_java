<template>
  <div>
    <el-button type="primary" @click="handleOpen">导入</el-button>
    <el-dialog title="导入数据" :visible.sync="open" width="20%" @close="handleClose">
      <el-upload class="upload-demo" action="#" :limit="1" :auto-upload="false" :show-file-list="true"
        :on-change="handleChangeEcho" :on-preview="handlePictureCardPreviewEcho" :on-remove="handleRemoveEcho"
        accept=".xlsx,.xls" :file-list="fileList">
        <el-button size="small" type="primary">点击上传</el-button>
        <div slot="tip" class="el-upload__tip">只能上传.xlsx文件</div>
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button @click="open = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

export default {
  components: {},
  props: {},
  data() {
    return {
      open: false,
      fileList: [],
    };
  },
  computed: {},
  watch: {},
  created() {

  },
  mounted() {

  },
  methods: {
    handleRemoveEcho(file, filesList) {
      this.fileList = filesList;
    },
    handlePictureCardPreviewEcho(file) { },
    handleChangeEcho(file, filesList) {
      this.fileList = filesList;
    },
    handleOpen() {
      this.open = true
    },
    
    handleClose() {
      this.fileList = []
    },
    importTemplate() {
      window.location.href = this.imgUrl + "fileResources/赠送卡批量导入模板.xlsx";
    },
  },
}
</script>
<style scoped></style>
