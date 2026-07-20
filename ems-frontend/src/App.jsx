import './App.css'
import EmployeeComponent from './components/EmployeeComponent'
import FooterComponent from './components/FooterComponent'
import HeaderComponent from './components/HeaderComponent'
import ListDepartmentComponent from './components/ListDepartmentComponent'
import ListEmployeeComponent from './components/ListEmployeeComponent'
import DepartmentComponent from './components/DepartmentComponent'
import { BrowserRouter, Routes, Route } from 'react-router-dom'

function App() {
  return (
    <BrowserRouter>
      <div className="app-container">

        <HeaderComponent />

        <main className="main-content">
          <Routes>

            <Route path='/' element={<ListEmployeeComponent />} />

            <Route path='/employees' element={<ListEmployeeComponent />} />

            <Route path='/add-employee' element={<EmployeeComponent />} />

            <Route path='/update-employee/:id' element={<EmployeeComponent />} />

            <Route path='/departments' element={<ListDepartmentComponent />} />

            <Route path='/add-department' element={<DepartmentComponent />} />

            <Route path='/edit-department/:id' element={<DepartmentComponent />} />

          </Routes>
        </main>

        <FooterComponent />

      </div>
    </BrowserRouter>
  )
}

export default App