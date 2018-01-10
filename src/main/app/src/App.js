import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';

const ReactDOM = require('react-dom');
const rest = require('rest');
const mime = require('rest/interceptor/mime');
const errorCode = require('rest/interceptor/errorCode');
class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            users: [],
            needHeader: true,
            needForm: false,
            needList:false,
            needAddForm: false,
            needLogout: false
        };
        this.tokenName = 'token';
        this.updateState = this.updateState.bind(this);
        this.fetchUsers = this.fetchUsers.bind(this);
    }

    updateState(state) {
        this.setState(state);
    }

    fetchUsers() {
        var client = rest.wrap(errorCode);
        client({
            path: 'http://localhost:8080/user/list',
            method: 'GET',
            headers: {'Authorization': localStorage.getItem(this.tokenName)},
        }).then(
            // resolve user list
            response => {
                this.setState({users: JSON.parse(response.entity), needForm: false, needList: true, needLogout: true})
            },
            // enable form rendering
            response => {
                localStorage.removeItem('token');
                this.setState({needForm: true, needList: false});
            }
        );
    }

    componentWillMount(){
        var token = localStorage.getItem(this.tokenName);
        if (token) this.fetchUsers();
        else this.setState({needForm: true});

    }

    render() {
        return (
            <div className="App">
                {this.state.needHeader ? <Header /> : null}
                {this.state.needForm ? <Form fetchUsers={this.fetchUsers} updateState={this.updateState}/> : null}
                {this.state.users.length && this.state.needList ? <UserList updateState={this.updateState} fetchUsers={this.fetchUsers} users={this.state.users}/> : null}
                {this.state.needLogout ? <Logout updateState={this.updateState}/> : null}
            </div>
        )
    }
}

class UserList extends React.Component{

    constructor(props){
        super(props);
        this.state = {name: '', email: '', password: ''};
        this.handleChange = this.handleChange.bind(this);
        this.handleClick = this.handleClick.bind(this);
    }

    handleChange(event) {
        var name = event.target.name;
        if (name === 'name') this.setState({name: event.target.value});
        if (name === 'email') this.setState({email: event.target.value});
        if (name === 'password') this.setState({password: event.target.value});
    }

    handleClick(event) {
        var client = rest.wrap(errorCode, { code: 400 });
        client({
            method: 'POST',
            path: 'http://localhost:8080/user/create',
            headers:{
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            },
            entity: JSON.stringify({name: this.state.name, email: this.state.email, password: this.state.password})
        }).then(response => {
                this.props.fetchUsers();
            },
            response => {
                localStorage.removeItem('token');
                this.props.updateState({needForm: true, needList: false});
            }
        )
        event.preventDefault();
    }

    render() {
        var users = this.props.users.map(user =>
            <User key={user.id} user={user} fetchUsers={this.props.fetchUsers} updateState={this.props.updateState}/>
        );
        return (
            <table className={'table'}>
                <tbody>
                <tr>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Password</th>
                    <th>Management</th>
                </tr>
                <tr>
                    <td>
                        <input className="form-control" type="text" name="name" value={this.state.name} onChange={this.handleChange}/>
                    </td>
                    <td>
                        <input className="form-control" type="email" name="email" value={this.state.email} onChange={this.handleChange}/>
                    </td>
                    <td>
                        <input className="form-control" type="password" name="password" value={this.state.password} onChange={this.handleChange}/>
                    </td>
                    <td>
                        <div className="form-group">
                            <button type="button" className="btn btn-success" onClick={this.handleClick}>Create</button>
                        </div>
                    </td>
                </tr>
                {users}
                </tbody>
            </table>
        )
    }
}

class User extends React.Component{

    constructor(props){
        super(props);
        this.state = {editMode: false};
        this.handleDelete = this.handleDelete.bind(this);
        this.handleEdit = this.handleEdit.bind(this);
        this.changeMode = this.changeMode.bind(this);
    }

    handleDelete(event){
        var client = rest.wrap(errorCode, { code: 400 });
        client({
            method: 'GET',
            path: 'http://localhost:8080/user/delete?id=' + this.props.user.id,
            headers:{
                'Authorization': localStorage.getItem('token')
            },
            params:{
                id: this.props.user.id
            },
        }).then(response => {
                if(response.entity === "true") this.props.fetchUsers();
            },
            response => {
                localStorage.removeItem('token');
                this.props.updateState({needForm: true, needList: false});
            }
        )
        event.preventDefault();
    }

    handleEdit(event) {
        this.setState({editMode: true});
        event.preventDefault();
    }

    changeMode(state) {
        this.setState(state);
    }

    render() {
        if (this.state.editMode) return <UserEditMode changeMode={this.changeMode} handleEdit={this.handleEdit} user={this.props.user} fetchUsers={this.props.fetchUsers} updateState={this.props.updateState}/>
        else return <UserViewMode user={this.props.user} handleDelete={this.handleDelete} handleEdit={this.handleEdit}/>
    }
}

class UserViewMode extends React.Component {

    render() {
        return (
            <tr>
                <td>{this.props.user.name}</td>
                <td>{this.props.user.email}</td>
                <td>{this.props.user.password}</td>
                <td>
                    <button type="button" className="btn btn-danger" onClick={this.props.handleDelete}>Delete</button>
                    <button type="button" className="btn btn btn-info" onClick={this.props.handleEdit}>Edit</button>
                </td>
            </tr>
        )
    }
}

class UserEditMode extends React.Component {

    constructor(props){
        super(props);
        this.state = {id:this.props.user.id, name:this.props.user.name, email:this.props.user.email, password:''};
        this.handleChange = this.handleChange.bind(this);
        this.handlePush = this.handlePush.bind(this);
        this.handleCancel = this.handleCancel.bind(this);
    }

    handleChange(event) {
        var name = event.target.name;
        if (name === 'name') this.setState({name: event.target.value});
        if (name === 'email') this.setState({email: event.target.value});
        if (name === 'password') this.setState({password: event.target.value});
        event.preventDefault();
    }

    handlePush(event) {
        var client = rest.wrap(errorCode, { code: 400 });
        client({
            method: 'POST',
            path: 'http://localhost:8080/user/update',
            headers:{
                'Content-Type': 'application/json',
                'Authorization': localStorage.getItem('token')
            },
            entity: JSON.stringify({id: this.state.id, name: this.state.name, email: this.state.email, password: this.state.password})
        }).then(response => {
                console.log(response);
                if (response.entity === 'true') {
                    this.props.changeMode({editMode: false});
                    this.props.fetchUsers();
                }
            },
            response => {
                localStorage.removeItem('token');
                this.props.updateState({needForm: true, needList: false});
            }
        )
        event.preventDefault();
    }

    handleCancel(event) {
        this.props.changeMode({editMode: false});
        event.preventDefault();
    }

    render() {
        return (
            <tr>
                <td><input className="form-control" type="text" name="name" value={this.state.name} onChange={this.handleChange}/></td>
                <td><input className="form-control" type="email" name="email" value={this.state.email} onChange={this.handleChange}/></td>
                <td><input className="form-control" type="password" name="password" value={this.state.password} onChange={this.handleChange}/></td>
                <td>
                    <button type="button" className="btn btn-primary" onClick={this.handlePush}>Push</button>
                    <button type="button" className="btn btn btn-info" onClick={this.handleCancel}>Cancel</button>
                </td>
            </tr>
        )
    }
}



class Header extends React.Component {
    render() {
        return (
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo" />
                <h1 className="App-title">Welcome to MyApp</h1>
            </header>
        )
    }
}
class Form extends React.Component {
    constructor(props) {
        super(props);
        this.state = {name: '', password: '', accessDenied: false};
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        var name = event.target.name;
        if (name === 'name') this.setState({name: event.target.value});
        if (name === 'password') this.setState({password: event.target.value});
    }

    handleSubmit(event) {
        // make request with user creds to obtain JWT
        var client = rest.wrap(errorCode, { code: 401 });
        client({
            method: 'POST',
            path: 'http://localhost:8080/login',
            headers:{
                'Content-Type': 'application/json'
            },
            entity: JSON.stringify({name: this.state.name, password: this.state.password})
        }).then(response => {
            localStorage.setItem('token', response.headers["Authorization"]);
            this.props.fetchUsers();
            this.props.updateState({needList: true, needForm: false});
        },
                response => {
            this.setState({accessDenied: true})
        }
        );
        event.preventDefault();
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
              { this.state.accessDenied ? <AccessDeniedMsg /> : null }
              <div className="form-group">
                  <input type="text" className="form-control" name="name" placeholder="enter name" value={this.state.name} onChange={this.handleChange}/>
              </div>
              <div className="form-group">
                  <input type="password" className="form-control" name="password" placeholder="enter password" value={this.state.password} onChange={this.handleChange}/>
              </div>
              <div className="form-group">
                  <input type="submit" className="btn btn-primary" value="Submit" />
              </div>
            </form>
        );
    }
}

class Logout extends React.Component {

    constructor(props){
        super(props);
        this.handleLogout = this.handleLogout.bind(this);
    }

    handleLogout(event) {
        rest({
            method: 'GET',
            path: 'http://localhost:8080/user/logout',
            headers:{
                'Authorization': localStorage.getItem('token')
            }
        }).then(response => {
                localStorage.removeItem('token');
                this.props.updateState({needForm: true, needList: false, needLogout: false});
            }
        );
        event.preventDefault();
    }

    render() {
        return (
            <button  className="btn" onClick={this.handleLogout}>Log out</button>
        )
    }
}

class AccessDeniedMsg extends React.Component {
    render(){
        return (
            <p><span className="badge badge-warning">401: Unauthorized</span></p>
        )
    }
}

export default App;
