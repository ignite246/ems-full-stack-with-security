import './App.css'
import CreateEmployeeComponent from './components/CreateEmployeeComponent'
import FooterComponent from './components/FooterComponent'
import HeaderComponent from './components/HeaderComponent'
import ListDepartmentComponent from './components/ListDepartmentComponent'
import ListEmployeeComponent from './components/ListEmployeeComponent'
import DepartmentComponent from './components/DepartmentComponent'
import { BrowserRouter, Routes, Route } from 'react-router-dom'

function App() {
  return (
    <>
      <BrowserRouter>
        <HeaderComponent />
        <Routes>
          {/*http://localhost:3000 */}
          <Route path='/' element={<ListEmployeeComponent />} />

          {/*http://localhost:3000/employees */}
          <Route path='/employees' element={<ListEmployeeComponent />} />

          {/*http://localhost:3000/add-employee */}
          <Route path='/add-employee' element={<CreateEmployeeComponent />} />

          {/*http://localhost:3000/update-employee/12 */}
          <Route path='/update-employee/:id' element={<CreateEmployeeComponent />} />

          <Route path='/departments' element={<ListDepartmentComponent />} />

          <Route path='/add-department' element={<DepartmentComponent />} />


        </Routes>
        <FooterComponent />
      </BrowserRouter>
    </>
  )
}

export default App
