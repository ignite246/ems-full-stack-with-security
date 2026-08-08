import './App.css'
import EmployeeComponent from './components/employee/EmployeeComponent'
import FooterComponent from './components/FooterComponent'
import HeaderComponent from './components/HeaderComponent'
import ListDepartmentComponent from './components/department/ListDepartmentComponent'
import ListEmployeeComponent from './components/employee/ListEmployeeComponent'
import DepartmentComponent from './components/department/DepartmentComponent'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import RegisterComponent from './components/auth/RegisterComponent'
import LoginComponent from './components/auth/LoginComponent'
import ProtectedRoute from './components/ProtectedRoute'
import PublicRoute from './components/PublicRoute'
import AdminRoute from './components/AdminRoute'
import PageNotFoundComponent from './components/PageNotFoundComponent'
import HealthDashboardComponent from './components/HealthDashboardComponent'

function App() {

  return (
    <div className="app-container">
      <HeaderComponent />
      <main className="main-content">
        <Routes>

          <Route path='/employees' element={
            <ProtectedRoute>
              <ListEmployeeComponent />
            </ProtectedRoute>
          } />

          <Route path='/add-employee' element={
            <ProtectedRoute>
              <EmployeeComponent />
            </ProtectedRoute>
          } />

          <Route path='/update-employee/:id' element={
            <ProtectedRoute>
              <EmployeeComponent />
            </ProtectedRoute>
          } />

          <Route path="/departments" element={
            <AdminRoute>
              <ListDepartmentComponent />
            </AdminRoute>
          }
          />

          <Route path="/add-department" element={
            <AdminRoute>
              <DepartmentComponent />
            </AdminRoute>
          }
          />

          <Route path="/edit-department/:id" element={
            <AdminRoute>
              <DepartmentComponent />
            </AdminRoute>
          }
          />

          <Route path="/" element={
            <PublicRoute>
              <LoginComponent />
            </PublicRoute>
          }
          />

          <Route path="/login" element={
            <PublicRoute>
              <LoginComponent />
            </PublicRoute>
          }
          />

          <Route path="/register" element={
            <PublicRoute>
              <RegisterComponent />
            </PublicRoute>
          }
          />

          <Route path="/health" element={
            <ProtectedRoute>
              <HealthDashboardComponent />
            </ProtectedRoute>
          }
          />

          <Route path="*" element={<PageNotFoundComponent />} />

        </Routes>
      </main>

      <FooterComponent />

    </div>
  )
}

export default App