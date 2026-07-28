import './App.css'
import EmployeeComponent from './components/EmployeeComponent'
import FooterComponent from './components/FooterComponent'
import HeaderComponent from './components/HeaderComponent'
import ListDepartmentComponent from './components/ListDepartmentComponent'
import ListEmployeeComponent from './components/ListEmployeeComponent'
import DepartmentComponent from './components/DepartmentComponent'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import RegisterComponent from './components/RegisterComponent'
import LoginComponent from './components/LoginComponent'
import { isUserLoggedIn } from './services/AuthService'
import ProtectedRoute from './components/ProtectedRoute'

function App() {

  return (
    <BrowserRouter>
      <div className="app-container">

        <HeaderComponent />

        <main className="main-content">
          <Routes>

            <Route path='/' element={<LoginComponent />} />

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

            <Route path='/departments' element={
              <ProtectedRoute>
                <ListDepartmentComponent />
              </ProtectedRoute>
            } />

            <Route path='/add-department' element={
              <ProtectedRoute>
                <DepartmentComponent />
              </ProtectedRoute>
            } />

            <Route path='/edit-department/:id' element={
              <ProtectedRoute>
                <DepartmentComponent />
              </ProtectedRoute>
            } />

            <Route path='/register' element={<RegisterComponent />} />

            <Route path='/login' element={<LoginComponent />} />

          </Routes>
        </main>

        <FooterComponent />

      </div>
    </BrowserRouter>
  )
}

export default App