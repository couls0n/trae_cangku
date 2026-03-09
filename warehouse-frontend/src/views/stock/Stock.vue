<template>
  <div class="stock-container">
    <el-card>
      <div class="toolbar">
        <el-select v-model="searchWarehouseId" placeholder="请选择仓库" clearable style="width: 150px">
          <el-option v-for="item in warehouseList" :key="item.id" :label="item.warehouseName" :value="item.id"></el-option>
        </el-select>
        <el-select v-model="searchProductId" placeholder="请选择商品" clearable filterable style="width: 200px">
          <el-option v-for="item in productList" :key="item.id" :label="item.productName" :value="item.id"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
      </div>
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="warehouseName" label="仓库"></el-table-column>
        <el-table-column prop="productName" label="商品"></el-table-column>
        <el-table-column prop="batchNo" label="批次号"></el-table-column>
        <el-table-column prop="quantity" label="库存数量"></el-table-column>
        <el-table-column prop="frozenQuantity" label="冻结数量"></el-table-column>
        <el-table-column prop="location" label="库位"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
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
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'Stock',
  data() {
    return {
      searchWarehouseId: null,
      searchProductId: null,
      warehouseList: [],
      productList: [],
      tableData: [],
      loading: false,
      current: 1,
      size: 10,
      total: 0
    }
  },
  created() {
    this.loadWarehouse()
    this.loadProduct()
    this.loadData()
  },
  methods: {
    async loadWarehouse() {
      try {
        const res = await request.get('/warehouse/list')
        this.warehouseList = res.data
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
        const res = await request.get('/stock/page', {
          params: {
            current: this.current,
            size: this.size,
            warehouseId: this.searchWarehouseId,
            productId: this.searchProductId
          }
        })
        this.tableData = res.data.records.map(item => {
          const warehouse = this.warehouseList.find(w => w.id === item.warehouseId)
          const product = this.productList.find(p => p.id === item.productId)
          return {
            ...item,
            warehouseName: warehouse ? warehouse.warehouseName : '',
            productName: product ? product.productName : ''
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
