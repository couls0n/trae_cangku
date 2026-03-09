<template>
  <div class="outbound-container">
    <el-card>
      <div class="toolbar">
        <el-input v-model="searchOrderNo" placeholder="请输入订单号" style="width: 200px" @keyup.enter.native="handleSearch" clearable></el-input>
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增出库单</el-button>
      </div>
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="orderNo" label="订单号"></el-table-column>
        <el-table-column prop="warehouseName" label="仓库"></el-table-column>
        <el-table-column prop="customerName" label="客户"></el-table-column>
        <el-table-column prop="totalAmount" label="总金额"></el-table-column>
        <el-table-column prop="status" label="状态">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orderTime" label="出库时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" @click="handleEdit(scope.row)" v-if="scope.row.status === 0">编辑</el-button>
            <el-button type="text" style="color: #67C23A" @click="handleConfirm(scope.row)" v-if="scope.row.status === 0">确认</el-button>
            <el-button type="text" style="color: #F56C6C" @click="handleDelete(scope.row.id)" v-if="scope.row.status === 0">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="current"
        :page-sizes="[10, 20, 50]"
        :page-size="size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        style="margin-top: 20px; text-align: right">
      </el-pagination>
    </el-card>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px">
      <el-form :model="form" :rules="rules" ref="form" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="仓库" prop="warehouseId">
              <el-select v-model="form.warehouseId" placeholder="请选择仓库" style="width: 100%">
                <el-option v-for="item in warehouseList" :key="item.id" :label="item.warehouseName" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户" prop="customerId">
              <el-select v-model="form.customerId" placeholder="请选择客户" style="width: 100%">
                <el-option v-for="item in customerList" :key="item.id" :label="item.customerName" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea"></el-input>
        </el-form-item>
        <el-divider>出库明细</el-divider>
        <el-button type="primary" size="small" @click="addItem" style="margin-bottom: 10px">添加商品</el-button>
        <el-table :data="form.items" border size="small">
          <el-table-column label="商品" width="200">
            <template slot-scope="scope">
              <el-select v-model="scope.row.productId" placeholder="选择商品" size="small" @change="val => onProductChange(val, scope.row)">
                <el-option v-for="item in productList" :key="item.id" :label="item.productName" :value="item.id"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="数量" width="120">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.quantity" :min="1" size="small" @change="calcTotal"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="单价" width="120">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.unitPrice" :precision="2" :min="0" size="small" @change="calcTotal"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="金额" width="100">
            <template slot-scope="scope">
              {{ (scope.row.quantity * scope.row.unitPrice).toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template slot-scope="scope">
              <el-button type="text" size="small" style="color: #F56C6C" @click="removeItem(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 10px; text-align: right; font-size: 16px;">
          合计金额：<span style="color: #F56C6C; font-weight: bold;">¥{{ form.totalAmount ? form.totalAmount.toFixed(2) : '0.00' }}</span>
        </div>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'Outbound',
  data() {
    return {
      searchOrderNo: '',
      warehouseList: [],
      customerList: [],
      productList: [],
      tableData: [],
      loading: false,
      current: 1,
      size: 10,
      total: 0,
      dialogVisible: false,
      dialogTitle: '',
      form: { items: [] },
      rules: {
        warehouseId: [{ required: true, message: '请选择仓库', trigger: 'change' }],
        customerId: [{ required: true, message: '请选择客户', trigger: 'change' }]
      }
    }
  },
  created() {
    this.loadWarehouse()
    this.loadCustomer()
    this.loadProduct()
    this.loadData()
  },
  methods: {
    getStatusType(status) {
      const types = { 0: 'warning', 1: 'success', 2: 'info' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { 0: '待确认', 1: '已完成', 2: '已取消' }
      return texts[status] || '未知'
    },
    async loadWarehouse() {
      try {
        const res = await request.get('/warehouse/list')
        this.warehouseList = res.data
      } catch (error) {
        console.error(error)
      }
    },
    async loadCustomer() {
      try {
        const res = await request.get('/customer/list')
        this.customerList = res.data
      } catch (error) {
        console.error(error)
      }
    },
    async loadProduct() {
      try {
        const res = await request.get('/product/list')
        this.productList = res.data
      } catch (error) {
        console.error(error)
      }
    },
    async loadData() {
      this.loading = true
      try {
        const res = await request.get('/outbound/page', {
          params: {
            current: this.current,
            size: this.size,
            orderNo: this.searchOrderNo
          }
        })
        this.tableData = res.data.records.map(item => {
          const warehouse = this.warehouseList.find(w => w.id === item.warehouseId)
          const customer = this.customerList.find(c => c.id === item.customerId)
          return {
            ...item,
            warehouseName: warehouse ? warehouse.warehouseName : '',
            customerName: customer ? customer.customerName : ''
          }
        })
        this.total = res.data.total
      } catch (error) {
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.current = 1
      this.loadData()
    },
    handleSizeChange(val) {
      this.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.current = val
      this.loadData()
    },
    handleAdd() {
      this.dialogTitle = '新增出库单'
      this.form = { status: 0, items: [], totalAmount: 0 }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },
    handleEdit(row) {
      this.dialogTitle = '编辑出库单'
      this.form = { ...row, items: [] }
      this.dialogVisible = true
    },
    handleView(row) {
      this.$message.info('查看功能待完善')
    },
    handleConfirm(row) {
      this.$confirm('确定要确认该出库单吗?', '提示', {
        type: 'warning'
      }).then(async () => {
        await request.put('/outbound', { ...row, status: 1 })
        this.$message.success('确认成功')
        this.loadData()
      }).catch(() => {})
    },
    handleDelete(id) {
      this.$confirm('确定要删除该出库单吗?', '提示', {
        type: 'warning'
      }).then(async () => {
        await request.delete(`/outbound/${id}`)
        this.$message.success('删除成功')
        this.loadData()
      }).catch(() => {})
    },
    addItem() {
      this.form.items.push({
        productId: null,
        quantity: 1,
        unitPrice: 0
      })
    },
    removeItem(index) {
      this.form.items.splice(index, 1)
      this.calcTotal()
    },
    onProductChange(val, row) {
      const product = this.productList.find(p => p.id === val)
      if (product) {
        row.unitPrice = product.price
      }
      this.calcTotal()
    },
    calcTotal() {
      this.form.totalAmount = this.form.items.reduce((sum, item) => {
        return sum + (item.quantity || 0) * (item.unitPrice || 0)
      }, 0)
    },
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          if (this.form.items.length === 0) {
            this.$message.warning('请添加出库商品')
            return
          }
          if (this.form.id) {
            await request.put('/outbound', this.form)
            this.$message.success('修改成功')
          } else {
            await request.post('/outbound', this.form)
            this.$message.success('新增成功')
          }
          this.dialogVisible = false
          this.loadData()
        }
      })
    }
  }
}
</script>

<style scoped>
.toolbar {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}
</style>
