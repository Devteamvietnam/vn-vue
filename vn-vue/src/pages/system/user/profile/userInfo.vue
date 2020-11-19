<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item label="Nickname" prop="nickName">
      <el-input v-model="user.nickName" />
    </el-form-item>
    <el-form-item label="Phone" prop="phonenumber">
      <el-input v-model="user.phonenumber" maxlength="11" />
    </el-form-item>
    <el-form-item label="Email" prop="email">
      <el-input v-model="user.email" maxlength="50" />
    </el-form-item>
    <el-form-item label="Gender">
      <el-radio-group v-model="user.sex">
        <el-radio label="0">Male</el-radio>
        <el-radio label="1">Female</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">Save</el-button>
      <el-button type="danger" size="mini" @click="close">Close</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import {updateUserProfile} from "@/services/api/system/user";

export default {
  props: {
    user: {
      type: Object
    }
  },
  data() {
    return {
      // form validation
      rules: {
        nickName: [
          {required: true, message: "User nickname cannot be empty", trigger: "blur"}
        ],
        email: [
          {required: true, message: "Email address cannot be empty", trigger: "blur" },
          {
            type: "email",
            message: "'Please enter the correct email address",
            trigger: ["blur", "change"]
          }
        ],
        phonenumber: [
          {required: true, message: "Mobile phone number cannot be empty", trigger: "blur" },
          {
            // pattern: /^1[3|4|5|6|7|8|9][0-9]\d{0}$/,
            message: "Please enter the correct mobile phone number",
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateUserProfile(this.user).then(response => {
            this.msgSuccess("Modified successfully");
          });
        }
      });
    },
    close() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({ path: "/index" });
    }
  }
};
</script>