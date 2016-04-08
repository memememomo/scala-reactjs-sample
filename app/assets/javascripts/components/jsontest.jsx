var PlaceForm = React.createClass({
  handleSubmit: function(e) {
    e.preventDefault();
    var nameElm = ReactDOM.findDOMNode(this.refs.name);
    var latElm = ReactDOM.findDOMNode(this.refs.lat);
    var longElm = ReactDOM.findDOMNode(this.refs.long);
    var name = nameElm.value.trim();
    var lat = latElm.value.trim();
    var long = longElm.value.trim();
    nameElm.value = '';
    latElm.value = '';
    longElm.value = '';
    var data = {
      name: name,
      location: {
        lat: parseFloat(lat),
        long: parseFloat(long)
      }
    };
    this.props.onSubmit(data);
  },
  render: function() {
    return (
      <form className="placeForm" onSubmit={this.handleSubmit}>
        <input type="text" placeholder="name" ref="name"/>
        <input type="text" placeholder="lat" ref="lat" />
        <input type="text" placeholder="long" ref="long" />
        <input type="submit" value="Create" />
      </form>
    );
  }
});

var PlaceList = React.createClass({
  render: function() {
    var placeUl = this.props.places.map(function(place) {
      return <li key={place.name}>{place.name}/{place.location.lat}/{place.location.long}</li>
    });
    return (
      <div>
        <ul>
          {placeUl}
        </ul>
      </div>
    );
  }
});

var Page = React.createClass({
  getInitialState: function() {
    return {places: []};
  },
  componentDidMount: function() {
    this.serverRequest = $.get("/places", function(result) {
      this.setState({places: result});
    }.bind(this));
  },
  componentWillUnmount: function() {
    this.serverRequest.abort();
  },
  onSubmit: function(place) {
     $.ajax({
      url: "/place/create",
      contentType: "application/json",
      dataType: "json",
      type: "POST",
      data: JSON.stringify(place),
      success: function(data) {
        var places = this.state.places;
        places.push(place);
        this.setState({places: places});
      }.bind(this),
      error: function(xhr, status, err) {
        console.log(xhr);
        console.log(status);
        console.log(err);
      }.bind(this)
    });
  },
  render: function() {
    return (
      <div>
        <PlaceForm onSubmit={this.onSubmit} />
        <PlaceList places={this.state.places}/>
      </div>
    );
  }
});

ReactDOM.render(
  <Page />,
  document.getElementById("main")
);